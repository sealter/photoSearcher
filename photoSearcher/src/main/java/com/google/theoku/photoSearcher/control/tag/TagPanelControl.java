package com.google.theoku.photoSearcher.control.tag;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

import com.google.theoku.photoSearcher.model.tag.TagPanelModel;

public class TagPanelControl {

	private final TagPanelModel mModel;


	public TagPanelControl(TagPanelModel pModel) {
		mModel = pModel;
	}
	
	public void addNewTag(String pText) {
		mModel.addTag(pText);
		
	}

	public DefaultListModel<String> getSelectedTagModel() {
		return mModel.getSelectedTagsModel();
	}

	public DefaultListModel<String> getAvailableTagModel() {
		return mModel.getAvailableTagsModel();
	}

	public void leftToRight() {
		DefaultListModel<String> aSelectedTagsModel = 
				mModel.getSelectedTagsModel();
		DefaultListSelectionModel aSelectedTagsSelectionModel =
				mModel.getSelectedTagsSelectionModel();
				
		List<String>selectedTags = new ArrayList<String>();
		
		for(int i=0;i<aSelectedTagsModel.size();i++) {
			
			if(aSelectedTagsSelectionModel.isSelectedIndex(i)) {
				selectedTags.add(aSelectedTagsModel.get(i));
			}
		}
		
		for(String aSelectedTag : selectedTags) {
			mModel.removeFromSelected(aSelectedTag);
			addNewTag(aSelectedTag);
		}
		
		
	}

	public ListSelectionModel getSelectedTagSelectionModel() {
		return mModel.getSelectedTagsSelectionModel();
	}

	public ListSelectionModel getAvailableTagSelectionModel() {
		return mModel.getAvailableSelectedTagSelectionModel();
	}

	public void rightToLeft() {
		DefaultListModel<String> availTagsModel = mModel.getAvailableTagsModel();
		DefaultListSelectionModel availSelection = mModel.getAvailableSelectedTagSelectionModel();
		
		
		for(int i=0;i<availTagsModel.size();i++) {
			
			if(availSelection.isSelectedIndex(i)) {
				mModel.addToSelected(availTagsModel.get(i));
			}
		}
		availSelection.clearSelection();
		
		
	}

}
