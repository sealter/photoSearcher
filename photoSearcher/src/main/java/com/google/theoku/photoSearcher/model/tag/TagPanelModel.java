package com.google.theoku.photoSearcher.model.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;

public class TagPanelModel {

	private final DefaultListModel<String>mSelectedTagsModel = new DefaultListModel<String>();
	private final DefaultListModel<String>mAvailableTagsModel = new DefaultListModel<String>();
	private final Set<String>mAllTags = new TreeSet<String>();
	private final Set<String>mSelectedTags = new TreeSet<String>();
	private final Set<String>mViewedAllTags = new TreeSet<String>();
	private final DefaultListSelectionModel mSelectedTagsSelectionModel = 
			new DefaultListSelectionModel();
	public DefaultListSelectionModel getSelectedTagsSelectionModel() {
		return mSelectedTagsSelectionModel;
	}
	public DefaultListSelectionModel getAvailableSelectedTagSelectionModel() {
		return mAvailableSelectedTagSelectionModel;
	}

	private final DefaultListSelectionModel mAvailableSelectedTagSelectionModel = 
			new DefaultListSelectionModel();
	public DefaultListModel<String> getSelectedTagsModel() {
		return mSelectedTagsModel;
	}
	public DefaultListModel<String> getAvailableTagsModel() {
		return mAvailableTagsModel;
	}

	public void addTag(String pTag) {
		if(mAllTags.add(pTag)) {
			mViewedAllTags.add(pTag);
			mAvailableTagsModel.clear();
			for(String aTag : mViewedAllTags) {
				mAvailableTagsModel.addElement(aTag);
			}
		}
	}
	public void addToSelected(String pString) {
		if(mSelectedTags.add(pString)) {
			mSelectedTagsModel.clear();

			for(String aString : mSelectedTags) {
				mSelectedTagsModel.addElement(aString);
			}
		}

	}
	public void removeFromSelected(String pSelectedTag) {
		if(mSelectedTags.remove(pSelectedTag)) {
			mSelectedTagsModel.removeElement(pSelectedTag);

		}

	}
	public void reset() {
		mSelectedTags.clear();
		mSelectedTagsModel.clear();
		mSelectedTagsSelectionModel.clearSelection();
		mAvailableSelectedTagSelectionModel.clearSelection();

	}
	public List<String> getselectedTags() {

		return new ArrayList<String>(mSelectedTags);
	}
	public void addTags(Collection<String> pTags) {

		for(String aTag : pTags) {
			addTag(aTag);
		}


	}

}
