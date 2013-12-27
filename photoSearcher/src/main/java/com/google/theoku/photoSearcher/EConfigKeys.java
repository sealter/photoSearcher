package com.google.theoku.photoSearcher;

public enum EConfigKeys {

	DB_NAME("mongoDB", "photos"),
	DB_COLLECTION("mongoCollection", "photoEntries"),
	DB_HOST("mongoHost", "127.0.0.1"),
	DB_PORT("mongoPort", "27017");
	
	private final String mKeyName;
	private final String mDefaultValue;

	private EConfigKeys(String pKeyName, String pDefaultValue) {
		mKeyName = pKeyName;
		mDefaultValue = pDefaultValue;
	}

	public String getKeyName() {
		return mKeyName;
	}

	public String getDefaultValue() {
		return mDefaultValue;
	}
}
