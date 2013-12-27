package com.google.theoku.photoSearcher.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.theoku.photoSearcher.control.PhotoSearcherControl;
import com.google.theoku.photoSearcher.model.IPhotoSearcherModelChangedListener;
import com.google.theoku.photoSearcher.model.PhotoSearcherModel;
import com.google.theoku.photoSearcher.view.tag.TagPanelView;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

public class PhotoSearcherView implements IPhotoSearcherModelChangedListener{

	private static final String DEFAULT_IMAGE_PATH_STRING = "Image Path: ";
	private static final String DEFAULT_DEST_STRING = "Destination: ";


	private class ImagePreview extends JPanel {
		private BufferedImage mImage;

		public ImagePreview(BufferedImage pImage) {
			mImage = pImage;

			updateSize();
		}

		private void updateSize() {
			if(mImage != null) {
				setPreferredSize(new Dimension(mImage.getWidth(), mImage.getHeight()));
			} else {
				setPreferredSize(new Dimension(500, 500));
			}
			revalidate();
		}

		@Override
		protected void paintComponent(Graphics pG) {
			super.paintComponent(pG);

			if(mImage != null) {
				pG.drawImage(mImage, 0, 0, getWidth(), getHeight(),null);

			}
		}

		public void updateImage(BufferedImage pImage) {
			mImage = pImage;
			updateSize();
			repaint();

		}

	}

	private final JPanel mView = new JPanel(new BorderLayout());
	private PhotoSearcherControl mControl;
	private JTextField mImagePath;
	private JButton mDestImage;
	private JTextField mDestString;
	private ImagePreview mImagePreview;
	private JDatePickerImpl mDatePicker;
	private JCheckBox mHasDate;

	public PhotoSearcherView(PhotoSearcherControl pControl) {

		mControl = pControl;
		JPanel topPanel = new JPanel(new GridLayout(2, 2));

		JButton loadImage = new JButton("Load Image");
		loadImage.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent pArg0) {
				mControl.loadImage(pArg0);
			}
		});
		topPanel.add(loadImage);

		mImagePath = new JTextField(DEFAULT_IMAGE_PATH_STRING,50);
		mImagePath.setEditable(false);
		topPanel.add(mImagePath);

		mDestImage = new JButton(DEFAULT_DEST_STRING);
		mDestImage.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent pArg0) {
				mControl.setDestination(pArg0);
			}
		});
		mDestImage.setEnabled(false);
		topPanel.add(mDestImage);

		mDestString = new JTextField(DEFAULT_DEST_STRING,50);
		topPanel.add(mDestString);



		JPanel metaPanel = new JPanel();
		metaPanel.setLayout(new BoxLayout(metaPanel, BoxLayout.Y_AXIS));

		JPanel datePanel = new JPanel();
		mHasDate = new JCheckBox("Date: ");
		JDatePicker aCreateJDatePanel = JDateComponentFactory.createJDatePicker();

		datePanel.add(mHasDate);
		if(aCreateJDatePanel instanceof JDatePickerImpl) {
			mDatePicker = (JDatePickerImpl)aCreateJDatePanel;
			mDatePicker.getModel().addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent pArg0) {
					if(mHasDate.isSelected()) {
						Date aSelectedDate = getSelectedDate();
						mControl.setDate(aSelectedDate);
					}
				}
			});
			datePanel.add(mDatePicker);
		}
		mHasDate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent pArg0) {

				if(mHasDate.isSelected()) {
					Date currentDate = getSelectedDate();
					mControl.setDate(currentDate);
				} else {
					mControl.setDate(null);
				}
			}
		});
		metaPanel.add(datePanel);


		TagPanelView aView = new TagPanelView(mControl.getTagPanelControl());
		metaPanel.add(aView.getView());

		mImagePreview = new ImagePreview(null);

		mView.add(topPanel, BorderLayout.NORTH);
		mView.add(new JScrollPane(mImagePreview), BorderLayout.CENTER);
		mView.add(metaPanel, BorderLayout.EAST);

		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent pArg0) {
				mControl.submitEntryToDB();
			}
		});


		mView.add(submit, BorderLayout.SOUTH);

		mControl.addModelUpdateListener(this);


	}




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
	private Container getView() {
		return mView;
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					createAndShowGUI();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		});
	}


	public void photoModelChanged(PhotoSearcherModel pModel) {
		System.out.println("Model Changed");

		File origFile = pModel.getOriginalFileLocation();

		if(origFile != null) {
			mDestImage.setEnabled(true);
			mImagePath.setText(DEFAULT_IMAGE_PATH_STRING + origFile.getPath());

			BufferedImage aRead = null;
			try {
				aRead = ImageIO.read(origFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mImagePreview.updateImage(aRead);
		} else {
			mImagePreview.updateImage(null);
			mDestImage.setEnabled(false);
			mImagePath.setText(DEFAULT_IMAGE_PATH_STRING);
		}

		File aDestination = pModel.getDestination();
		if(aDestination != null) {
			mDestString.setText(aDestination.getPath());
		} else {
			mDestString.setText(DEFAULT_DEST_STRING);
		}

		mDestImage.setEnabled(pModel.getOriginalFileLocation() != null);
		
		if(pModel.getDate() != null) {
			mHasDate.setSelected(true);
		}
	}


	private Date getSelectedDate() {
		int aDay = mDatePicker.getModel().getDay();
		int aMonth = mDatePicker.getModel().getMonth();
		int aYear = mDatePicker.getModel().getYear();

		Calendar aInstance = Calendar.getInstance();
		aInstance.set(aYear, aMonth, aDay);
		Date currentDate = aInstance.getTime();
		return currentDate;
	}
}
