package com.google.theoku.photoSearcher.control;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.theoku.photoSearcher.EConfigKeys;
import com.google.theoku.photoSearcher.control.tag.TagPanelControl;
import com.google.theoku.photoSearcher.model.IPhotoSearcherModelChangedListener;
import com.google.theoku.photoSearcher.model.PhotoRecord;
import com.google.theoku.photoSearcher.model.PhotoRecordModel;
import com.google.theoku.photoSearcher.model.PhotoSearcherModel;
import com.google.theoku.photoSearcher.utils.ImageFileFilter;
import com.google.theoku.photoSearcher.utils.Utils;
import com.google.theoku.photoSearcher.view.PhotoRecordView;
import com.google.theoku.photoSearcher.view.PhotoSearcherView;
import com.google.theoku.photoSearcher.view.tag.TagPanelView;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBPort;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * This is responsible for maintaing the {@link PhotoSearcherModel} as well
 * as doing most of the work of the PhotoSearcher.
 * @author lordoku
 *
 */
public class PhotoSearcherControl {

	/**
	 * Commons Logging
	 */
	private static final Log LOGGER = 
			LogFactory.getLog(PhotoSearcherControl.class);

	/**
	 * {@link JFileChooser} that's used to load the initial image
	 */
	private JFileChooser mOpenChooser = new JFileChooser();

	/**
	 * The {@link JFileChooser} that's used to set the destination of 
	 * the image
	 */
	private JFileChooser mDestinationChooser = new JFileChooser();

	/**
	 * The model portion of the PhotoSearcher. The {@link PhotoSearcherControl}
	 * will update the state of this object based on the different commands, usually
	 * from the view. 
	 */
	private final PhotoSearcherModel mModel;

	/**
	 * The Client interface to the Mongo Application
	 */
	private final MongoClient mongoClient;

	/**The interface to the database within the Mongo Application**/
	private final DB mDb;

	/**The interface to the collection within the database, that is referenced
	 * in the mDB. 
	 */
	private DBCollection mTable;

	/**
	 * Control for the {@link TagPanelView}. This is used to tag the images
	 */
	private TagPanelControl mTagPanelControl;

	/**
	 * Constructor for the {@link PhotoSearcherControl}. 
	 * @param pModel
	 * 	The model that is used to keep track of the state of the Photo Searcher. 
	 * The {@link PhotoSearcherControl} will update the state of this class. 
	 * @throws UnknownHostException
	 */
	public PhotoSearcherControl(PhotoSearcherModel pModel) throws UnknownHostException {

		Map<EConfigKeys, String>keyValues = 
				new EnumMap<EConfigKeys, String>(EConfigKeys.class);

		try {
			PropertiesConfiguration aConfig = 
					new PropertiesConfiguration("photoSearcher.properties");

			for(EConfigKeys aKey : EConfigKeys.values()) {

				String value = aConfig.getString(aKey.getKeyName());

				if(LOGGER.isTraceEnabled()) {
					LOGGER.trace("Loading: key= " + aKey +", value= " + value);
				}
				keyValues.put(aKey, value);

			}

		} catch (ConfigurationException e) {
			LOGGER.error("Unable to load config file!", e);
		}


		for(EConfigKeys aKey : EConfigKeys.values()) {

			if(keyValues.get(aKey) == null) {

				if(LOGGER.isWarnEnabled()) {
					LOGGER.warn("Key: " + aKey + " was not set correctly, using default: " + aKey.getDefaultValue());
				}
				keyValues.put(aKey, aKey.getDefaultValue());
			}
		}
		int portNum = DBPort.PORT;

		try {
			portNum = Integer.parseInt(keyValues.get(EConfigKeys.DB_PORT));
		} catch(NumberFormatException e) {
			LOGGER.error("Port number was: " + 
					keyValues.get(EConfigKeys.DB_PORT) + 
					" which is not a number. Using DB Port default= "
					+ DBPort.PORT,e);
		}

		ServerAddress mongoAddress = new ServerAddress(
				keyValues.get(EConfigKeys.DB_HOST),portNum);

		mTagPanelControl = new TagPanelControl(pModel.getTagPanelModel());

		mongoClient = new MongoClient(mongoAddress);

		mDb = mongoClient.getDB(keyValues.get(EConfigKeys.DB_NAME));

		mTable = mDb.getCollection(keyValues.get(EConfigKeys.DB_COLLECTION));

		mModel = pModel;
		
		Set<String>tags = Utils.getAllTags(mTable);
		
		mModel.getTagPanelModel().addTags(tags);

		mOpenChooser.setFileFilter(new ImageFileFilter());
	}

	/**
	 * Command to launch the {@link JFileChooser} responsible for selecting
	 * an image to load. This method will wait for the {@link JFileChooser} to 
	 * close and decide what to do. If an image is selected and the approve 
	 * option is made, then this method will update the {@link PhotoSearcherModel}
	 * to set the selected image.
	 * @param pArg0
	 * 	This is the action event that fired off the loading of the image. Currently
	 * this is used to get the component.
	 * 
	 * TODO: Does it make more sense for the file chooser to live in the {@link PhotoSearcherView}?
	 * This way the {@link PhotoSearcherView} won't have any GUI components and 
	 * can be further abstracted out. If this is the case, this method should 
	 * be updated to take in a string or {@link File} to the image itself. 
	 */
	public void loadImage(ActionEvent pArg0) {

		int aShowOpenDialog = mOpenChooser.showOpenDialog((Component)pArg0.getSource());

		switch(aShowOpenDialog) {
		case JFileChooser.APPROVE_OPTION:
			mModel.reset();

			File aSelectedFile = mOpenChooser.getSelectedFile();
			mModel.setOriginalFileLocation(aSelectedFile);
			break;
		}

	}

	/**
	 * Method to register for changes to the {@link PhotoSearcherModel}
	 * @param pListener
	 * 	The Listener to start receiving notifications of model updates
	 */
	public void addModelUpdateListener(IPhotoSearcherModelChangedListener pListener) {
		mModel.addModelUpdateListener(pListener);

	}

	/**
	 * Method to update the destination of the image. This will launch the {@link JFileChooser}
	 * responsible for setting the destination. Once the location and the 
	 * approve option has been selected, the {@link PhotoSearcherModel} will be 
	 * updated to set the destination. 
	 * 
	 * TODO: Like the loadImage method, does it make more sense for the 
	 * {@link JFileChooser} to live in the {@link PhotoSearcherView}?
	 * @param pArg0
	 * 	The event that fired the updateDestination
	 */
	public void setDestination(ActionEvent pArg0) {

		mDestinationChooser.setSelectedFile(mModel.getOriginalFileLocation());
		int aShowSaveDialog = mDestinationChooser.showSaveDialog((Component)pArg0.getSource());

		switch(aShowSaveDialog) {
		case JFileChooser.APPROVE_OPTION: 

			File saveFile = mDestinationChooser.getSelectedFile();
			mModel.setDestination(saveFile);
			break;
		}
	}

	/**
	 * Method to set the date of the image. This is an optional field
	 * @param pCurrentDate
	 * 	The date to set. null is valid; this means that the date is not 
	 * going to be used. 
	 */
	public void setDate(Date pCurrentDate) {
		mModel.setDate(pCurrentDate);

	}

	/**
	 * Method that will take the current state from the {@link PhotoSearcherModel}
	 * and store it into the {@link DBCollection}. Once the entry has been sent
	 * correctly, this should clear out the PhotoSearcher so that another image
	 * can be loaded. 
	 * 
	 * TODO: This is currently being run on the AWT-EventQueue. May want to consider
	 * offloading the store to another thread so that the GUI does not get hung.
	 */
	public void submitEntryToDB() {
		BasicDBObject document = new BasicDBObject();
		document.put("File Location", mModel.getDestination().getAbsolutePath());

		if(mModel.getDate() != null) {
			document.put("Date", mModel.getDate());
		}


		List<String> tags = mModel.getTagPanelModel().getselectedTags();
		if(tags != null && !tags.isEmpty()) {

			List<BasicDBObject>tagList = new ArrayList<BasicDBObject>();
			for(String aTag : tags) {
				BasicDBObject aTagObj = new BasicDBObject();
				aTagObj.put("Tag", aTag);
				tagList.add(aTagObj);
			}
			document.put("Tags", tagList);
		}


		try {
			BufferedImage aRead = ImageIO.read(mModel.getOriginalFileLocation());
			ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
			ImageIO.write(aRead, "jpg", baos);
			baos.flush();

			document.put("Image", baos.toByteArray());
		} catch (IOException e) {
			if(LOGGER.isErrorEnabled()) {
				LOGGER.error("Unable to read Image, will not include in DB!", e);
			}
		}
		mModel.getOriginalFileLocation();

		mTable.insert(document);

		mModel.getOriginalFileLocation().renameTo(mModel.getDestination());
		mModel.reset();

	}

	public TagPanelControl getTagPanelControl() {
		return mTagPanelControl;
	}

	public void loadImageFromDB() {

		List<PhotoRecord> records = Utils.loadRecords(mTable);
		
		PhotoRecordModel aModel = 
				new PhotoRecordModel(records);
		PhotoRecordControl aControl = new PhotoRecordControl(aModel, mTable);
		PhotoRecordView aView = new PhotoRecordView(aControl);
		
		JFrame aFrame = new JFrame();
		aFrame.setContentPane(aView.getView());
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.pack();
		aFrame.setVisible(true);

	}



}


