package com.google.theoku.photoSearcher.model;

import java.util.List;

public class PhotoRecord {
	
	public byte[] getImageData() {
		return mImageData;
	}
	public PhotoRecord(byte[] pImageData, String pFileLocation,
			List<String> pTags) {
		super();
		mImageData = pImageData;
		mFileLocation = pFileLocation;
		mTags = pTags;
	}
	public String getFileLocation() {
		return mFileLocation;
	}
	public List<String> getTags() {
		return mTags;
	}
	final byte [] mImageData;
	final String mFileLocation;
	final List<String>mTags;
}
