package org.chicktech.models;

import java.io.Serializable;

public class CTEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6878520745648415077L;
	
	
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
