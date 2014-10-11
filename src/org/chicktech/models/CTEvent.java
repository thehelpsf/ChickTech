package org.chicktech.models;

public class CTEvent {
	private String mName;
	private String mDescription;
	
	public CTEvent(String name, String description) {
		mName = name;
		mDescription = description;
	}

	public String getName() {
		return mName;
	}

	public String getDescription() {
		return mDescription;
	}
}
