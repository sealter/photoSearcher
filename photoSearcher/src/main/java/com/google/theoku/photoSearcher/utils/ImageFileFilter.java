package com.google.theoku.photoSearcher.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter{

	@Override
	public String getDescription() {
		return "Images";
	}

	@Override
	public boolean accept(File pF) {

		if(pF.isDirectory()) {
			return true;
		}
		return Utils.isImage(pF);


	}
}
