package com.google.theoku.photoSearcher.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.theoku.photoSearcher.model.PhotoRecord;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
	
	public static List<PhotoRecord> loadRecords(DBCollection pTable) {
		List<PhotoRecord>records = new ArrayList<PhotoRecord>();
		
		DBCursor aCursor = pTable.find();

		while(aCursor.hasNext()) {
			DBObject anObj = aCursor.next();

			byte [] imageData = null;
			String fileLocation = null;
			List<String>tags = new ArrayList<String>();

			if(anObj.containsField("Image")) {

				Object aValue = anObj.get("Image");

				if(aValue instanceof byte[]) {
					imageData = (byte[])aValue;
				}
			}

			if(anObj.containsField("File Location")) {
				fileLocation = (String)anObj.get("File Location");
			}

			if(anObj.containsField("Tags")) {

				Object aObject = anObj.get("Tags");

				if(anObj instanceof List<?>) {
					List<?>tagList = (List<?>)anObj;

					for(Object aTagObj : tagList) {
						if(aTagObj instanceof BasicDBObject) {
							BasicDBObject tagObj = (BasicDBObject)aTagObj;
							if(tagObj.containsField("Tag")) {
								tags.add(tagObj.getString("Tag"));
							}
						}

					}
				}
			}
			

			PhotoRecord aRecord = 
					new PhotoRecord(imageData,
							fileLocation,
							tags);
			
			records.add(aRecord);
		}
		return records;
	}
	
	private Utils() {
		
	}
}
