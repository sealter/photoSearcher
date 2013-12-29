package com.google.theoku.photoSearcher.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.google.theoku.photoSearcher.control.PhotoRecordControl;
import com.google.theoku.photoSearcher.model.PhotoRecord;
import com.google.theoku.photoSearcher.model.PhotoRecordModel;
import com.google.theoku.photoSearcher.utils.ImagePanel;

public class PhotoRecordView {
	
	private PhotoRecordControl mControl;
	private final JPanel mView = new JPanel(new BorderLayout());
	

	public PhotoRecordView(PhotoRecordControl pControl) {
		mControl = pControl;
		final PhotoRecordModel aModel = mControl.getModel();
		
		final JTable aTable = new JTable(aModel.getTableModel());
		
		JPanel buttonPanel = new JPanel();
		JButton showImage = new JButton("Show Image");
		showImage.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent pE) {
				int aSelectedRow = aTable.getSelectedRow();
				PhotoRecord aRecord = 
						aModel.getTableModel().getRow(aSelectedRow);
				
				if(aRecord.getImageData() != null) {
					ByteArrayInputStream bin = new ByteArrayInputStream(aRecord.getImageData());
					
					try {
						BufferedImage aRead = ImageIO.read(bin);
						ImagePanel.showImage(aRead);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
			}
		});
		buttonPanel.add(showImage);
		
		JButton reload = new JButton("Reload");
		reload.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent pArg0) {
				mControl.reload();
			}
		});
		
		buttonPanel.add(reload);
		JScrollPane aPane = new JScrollPane(aTable);
		
		
		JList<String>tagListView= new JList<String>(aModel.getTagListModel());
		tagListView.setSelectionModel(aModel.getTagListSelectionModel());
		
		
		mView.add(aPane, BorderLayout.CENTER);
		mView.add(buttonPanel, BorderLayout.SOUTH);
		mView.add(new JScrollPane(tagListView), BorderLayout.EAST);
	}
	
	public JPanel getView() {
		return mView;
	}

}
