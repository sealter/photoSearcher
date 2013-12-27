package com.google.theoku.photoSearcher.view.tag;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.google.theoku.photoSearcher.control.tag.TagPanelControl;
import com.google.theoku.photoSearcher.model.tag.TagPanelModel;

public class TagPanelView {

	private final JPanel mView = new JPanel(new BorderLayout());
	private final TagPanelControl mControl;
	public TagPanelView(TagPanelControl pControl) {
		
		mControl = pControl;
		JPanel tagSelectionPanel = new JPanel(new GridLayout(1, 2));
		
		
		JList<String>selectedTags = new JList<String>(mControl.getSelectedTagModel());
		selectedTags.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selectedTags.setSelectionModel(mControl.getSelectedTagSelectionModel());
		
		JList<String>availableTags = new JList<String>(mControl.getAvailableTagModel());
		availableTags.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		availableTags.setSelectionModel(mControl.getAvailableTagSelectionModel());
		
		JScrollPane selectPane = new JScrollPane(selectedTags);
		selectPane.setBorder(BorderFactory.createTitledBorder("Selected"));
		
		JScrollPane availPane = new JScrollPane(availableTags);
		availPane.setBorder(BorderFactory.createTitledBorder("Avail"));
		
		tagSelectionPanel.add(selectPane);
		tagSelectionPanel.add(availPane);
		
		
		JPanel buttonPanel = new JPanel();
		
		JButton leftToRightButton = new JButton("-->");
		leftToRightButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent pArg0) {
				mControl.leftToRight();
			}
		});
		JButton rightToLeftButton = new JButton("<--");
		rightToLeftButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent pArg0) {
				mControl.rightToLeft();
			}
		});
		
		
		JButton addNewTagButton = new JButton("Add Tag");
		final JTextField aField = new JTextField(20);
		addNewTagButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent pArg0) {
				String aText = aField.getText();
				if(aText != null && !aText.isEmpty()) {
					mControl.addNewTag(aText);
					aField.setText("");
				}
			}
		});
		buttonPanel.add(leftToRightButton);
		buttonPanel.add(rightToLeftButton);
		buttonPanel.add(addNewTagButton);
		buttonPanel.add(aField);
		
		
		mView.add(tagSelectionPanel, BorderLayout.CENTER);
		mView.add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				createAndShowGUI();
			}
		});
	}


	protected static void createAndShowGUI() {
		JFrame aFrame = new JFrame();
		
		TagPanelModel aModel = new TagPanelModel();
		TagPanelControl aControl = new TagPanelControl(aModel);
		TagPanelView aView = new TagPanelView(aControl);
		
		aFrame.setContentPane(aView.getView());
		aFrame.pack();
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setVisible(true);
		
	}


	public Container getView() {
		// TODO Auto-generated method stub
		return mView;
	}
}
