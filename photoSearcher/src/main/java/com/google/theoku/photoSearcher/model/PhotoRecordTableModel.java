package com.google.theoku.photoSearcher.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class PhotoRecordTableModel extends DefaultTableModel {

	private final List<PhotoRecord>mRecords = new ArrayList<PhotoRecord>();
	
	public void addRecord(PhotoRecord pRecord) {
		mRecords.add(pRecord);
		fireTableRowsInserted(mRecords.size()-1, mRecords.size()-1);
	}
	
	public void removeRecord(PhotoRecord pRecord) {
		int idx = mRecords.indexOf(pRecord);
		mRecords.remove(pRecord);
		
		fireTableRowsDeleted(idx, idx);
	}
	
	@Override
	public Object getValueAt(int pRow, int pColumn) {
		Object returnVal = null;
		
		if(mRecords != null) {
			PhotoRecord aPhotoRecord = mRecords.get(pRow);
			
			switch(pColumn) {
			case 0:
				returnVal = aPhotoRecord.getFileLocation();
				break;
			case 1:
				returnVal = aPhotoRecord.getTags();
				break;
			}
		}
		
		return returnVal;
	}
	
	@Override
	public Class<?> getColumnClass(int pColumnIndex) {
		switch(pColumnIndex) {
		case 0:
			return String.class;
		case 1:
			return List.class;
		}
		
		return Object.class;
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public String getColumnName(int pColumn) {
		switch(pColumn) {
		case 0: return "Loc";
		case 1: return "Tags";
		}
		return "";
	}
	
	@Override
	public int getRowCount() {
		int count = 0;
		
		if(mRecords != null) {
			count = mRecords.size();
		}
		
		return count;
	}
	
	@Override
	public boolean isCellEditable(int pRow, int pColumn) {
		return false;
	}

	public PhotoRecord getRow(int pRow) {
		return mRecords.get(pRow);
	}

	public void clear() {
		int aSize = mRecords.size();
		mRecords.clear();
		fireTableRowsDeleted(0, aSize-1);
		
	}
	
	public void setRecords(List<PhotoRecord>pRecords) {
		clear();
		mRecords.addAll(pRecords);
		fireTableRowsInserted(0, pRecords.size()-1);
	}
	
	
	
}
