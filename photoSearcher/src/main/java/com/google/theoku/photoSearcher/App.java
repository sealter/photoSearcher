package com.google.theoku.photoSearcher;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.theoku.photoSearcher.control.PhotoSearcherControl;
import com.google.theoku.photoSearcher.model.PhotoSearcherModel;
import com.google.theoku.photoSearcher.view.PhotoSearcherView;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


/**
 * Hello world!
 *
 */
public class App {
	
	private static final Log LOGGER = LogFactory.getLog(App.class);
	
	public static void createAndShowGUI() throws UnknownHostException {
		JFrame aFrame = new JFrame();

		PhotoSearcherModel aModel = new PhotoSearcherModel();
		PhotoSearcherControl aControl = new PhotoSearcherControl(aModel);

		PhotoSearcherView aView = new PhotoSearcherView(aControl);

		aFrame.setContentPane(aView.getView());
		aFrame.pack();
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Starting application!");
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					createAndShowGUI();
				} catch (UnknownHostException e) {
					if(LOGGER.isErrorEnabled()) {
						LOGGER.error("Unable to start application; unable to connect to DB: ", e);
					}
				}
			}
		});
		
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finished Main Thread");
		}
	}
}
