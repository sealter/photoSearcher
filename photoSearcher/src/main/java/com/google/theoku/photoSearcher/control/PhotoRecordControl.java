package com.google.theoku.photoSearcher.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.theoku.photoSearcher.model.PhotoRecord;
import com.google.theoku.photoSearcher.model.PhotoRecordModel;
import com.google.theoku.photoSearcher.utils.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class PhotoRecordControl implements ListSelectionListener {

	private PhotoRecordModel mModel;
	private DBCollection mTable;

	public PhotoRecordControl(PhotoRecordModel pModel, DBCollection pTable) {
		mModel = pModel;
		mTable = pTable;

		Set<String> aAllTags = Utils.getAllTags(mTable);

		for(String aString : aAllTags) {
			mModel.getTagListModel().addElement(aString);
		}

		mModel.getTagListSelectionModel().addListSelectionListener(this);

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

	public void valueChanged(ListSelectionEvent pArg0) {

		if(!pArg0.getValueIsAdjusting()) {
			BasicDBList aList = new BasicDBList();
			for(int i=0;i<mModel.getTagListModel().getSize(); i++) {

				if(mModel.getTagListSelectionModel().isSelectedIndex(i)) {
					BasicDBObject anObj = new BasicDBObject();
					anObj.put("Tag", mModel.getTagListModel().get(i));
					aList.add(anObj);
				}
			}
			
			DBObject inClause = new BasicDBObject("$all", aList);
			DBObject query = new BasicDBObject("Tags", inClause);
			
			DBCursor aCursor = mTable.find(query);
			mModel.getTableModel().clear();
			while(aCursor.hasNext()) {
				DBObject anElement = aCursor.next();
				PhotoRecord aRecord = Utils.getRecord(anElement);
				mModel.getTableModel().addRecord(aRecord);
			}
		}
	}
}
