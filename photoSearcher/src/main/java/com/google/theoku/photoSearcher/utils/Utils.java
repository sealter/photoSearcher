package com.google.theoku.photoSearcher.utils;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Utils {
	public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    
    public static final Set<String>IMAGE_TYPES;
    static {
    	
    	Set<String>tempSet = new HashSet<String>();
    	tempSet.add(jpeg);
    	tempSet.add(jpg);
    	tempSet.add(gif);
    	tempSet.add(tif);
    	tempSet.add(tiff);
    	tempSet.add(png);
    	
    	IMAGE_TYPES = Collections.unmodifiableSet(tempSet);
    	
    }

	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
	public static boolean isImage(File pFile) {
		
		return IMAGE_TYPES.contains(getExtension(pFile));
	}
	
	private Utils() {
		
	}
}
