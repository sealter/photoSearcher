package com.google.theoku.photoSearcher.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImagePanel extends JPanel{
	
	private BufferedImage mImage;

	public ImagePanel(BufferedImage pImage) {
		mImage = pImage;
		setPreferredSize(new Dimension(pImage.getWidth(), pImage.getHeight()));
	}
	
	@Override
	protected void paintComponent(Graphics pArg0) {
		super.paintComponent(pArg0);
		
		pArg0.drawImage(mImage, 0, 0, getWidth(), getHeight(), null);
	}
	
	public static void showImage(BufferedImage pImage) {
		JFrame aFrame = new JFrame();
		ImagePanel aPanel = new ImagePanel(pImage);
		
		
		JScrollPane aPane = new JScrollPane(aPanel);
		aFrame.setContentPane(aPane);
		aFrame.pack();
		aFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aFrame.setVisible(true);
		
	}

}
