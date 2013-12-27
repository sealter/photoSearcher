package com.google.theoku.photoSearcher.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.DefaultListModel;

import com.google.theoku.photoSearcher.model.tag.TagPanelModel;

public class PhotoSearcherModel {
	
	private Date mDate;

	private File mDestination;

	private final Set<IPhotoSearcherModelChangedListener>mListenes = 
			new CopyOnWriteArraySet<IPhotoSearcherModelChangedListener>();
	

	private File mOriginalFileLocation = null;
	
	private final TagPanelModel mTagPanelModel = new TagPanelModel();
	
	public TagPanelModel getTagPanelModel() {
		return mTagPanelModel;
	}
	public void addModelUpdateListener(IPhotoSearcherModelChangedListener pPhotoView) {
		
		mListenes.add(pPhotoView);
	}
	public Date getDate() {
		return mDate;
	}

	public File getDestination() {
		return mDestination;
	}
	
	public File getOriginalFileLocation() {
		return mOriginalFileLocation;
	}

	private void notifyListeners() {
		
		for(IPhotoSearcherModelChangedListener aListener : mListenes) {
			aListener.photoModelChanged(this);
		}
	}
	
	public void reset() {
		mDestination = null;
		mOriginalFileLocation = null;
		mTagPanelModel.reset();
	
		notifyListeners();
		
	}

	public void setDate(Date pDate) {
		mDate = pDate;
		notifyListeners();
	}

	public void setDestination(File pSaveFile) {
		mDestination = pSaveFile;
		notifyListeners();
		
	}

	public void setOriginalFileLocation(File pOriginalFileLocation) {
		mOriginalFileLocation = pOriginalFileLocation;
		notifyListeners();
	}

}
