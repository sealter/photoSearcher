package com.google.theoku.photoSearcher.model;

import java.util.List;

public class PhotoRecordModel {
	
	private final PhotoRecordTableModel mTableModel = 
			new PhotoRecordTableModel();

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
