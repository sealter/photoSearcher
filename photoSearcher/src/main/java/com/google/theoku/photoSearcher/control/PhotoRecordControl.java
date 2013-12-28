package com.google.theoku.photoSearcher.control;

import java.util.List;

import com.google.theoku.photoSearcher.model.PhotoRecord;
import com.google.theoku.photoSearcher.model.PhotoRecordModel;
import com.google.theoku.photoSearcher.utils.Utils;
import com.mongodb.DBCollection;


public class PhotoRecordControl {
	
	private PhotoRecordModel mModel;
	private DBCollection mTable;

	public PhotoRecordControl(PhotoRecordModel pModel, DBCollection pTable) {
		mModel = pModel;
		mTable = pTable;
	}
	
	public void addRecord(PhotoRecord pRecord) {
		mModel.addRecord(pRecord);
	}

	public PhotoRecordModel getModel() {
		return mModel;
	}

	public void reload() {
		List<PhotoRecord> aLoadRecords = Utils.loadRecords(mTable);
		mModel.setRecords(aLoadRecords);
		
	}
}
