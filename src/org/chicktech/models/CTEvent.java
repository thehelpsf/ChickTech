package org.chicktech.models;

import android.os.Parcel;
import android.os.Parcelable;


public class CTEvent implements Parcelable {
	
	private String mName;
	private String mDescription;
	
	// normal constructor
	public CTEvent(String name, String description) {
		mName = name;
		mDescription = description;
	}

	// constructor for Parcel
	private CTEvent(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		//out.writeInt(mData);
        out.writeString(mName);
        out.writeString(mDescription);
	}

	public static final Parcelable.Creator<CTEvent> CREATOR = new Parcelable.Creator<CTEvent>() {

		@Override
		public CTEvent createFromParcel(Parcel in) {
			return new CTEvent(in);
		}

		@Override
		public CTEvent[] newArray(int size) {
			return new CTEvent[size];
		}
		
	};
	
	// getters / setters
	
    public String getName() {
		return mName;
	}

	public String getDescription() {
		return mDescription;
	}



}
