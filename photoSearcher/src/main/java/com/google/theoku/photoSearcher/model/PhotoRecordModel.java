package com.google.theoku.photoSearcher.model;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;

public class PhotoRecordModel {
	
	private final PhotoRecordTableModel mTableModel = 
			new PhotoRecordTableModel();
	
	private final DefaultListModel<String>mTagListModel =
			new DefaultListModel<String>();
	
	public DefaultListModel<String> getTagListModel() {
		return mTagListModel;
	}

	public DefaultListSelectionModel getTagListSelectionModel() {
		return mTagListSelectionModel;
	}

	private final DefaultListSelectionModel mTagListSelectionModel =
			new DefaultListSelectionModel();

	public PhotoRecordModel(List<PhotoRecord> pRecords) {
		
		for(PhotoRecord aRecord : pRecords) {
			mTableModel.addRecord(aRecord);
		}
	}

	public PhotoRecordTableModel getTableModel() {
		return mTableModel;
	}

	public void addRecord(PhotoRecord pRecord) {
		mTableModel.addRecord(pRecord);
		
	}

	public void setRecords(List<PhotoRecord> pLoadRecords) {
		mTableModel.setRecords(pLoadRecords);
		
	}

}
