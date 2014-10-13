package org.chicktech.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;


public class CTEvent implements Parcelable {
	
	private static final int MS_PER_HOUR = 1 * 60 * 60 * 1000;
	private static final int MS_PER_DAY = 24 * MS_PER_HOUR;
	
	private String mName;
	private String mDescription;
	private long mStart; 	// timestamp milliseconds since epoch.
	private long mEnd; 	
	private String mLocation;
	
	// normal constructor
	public CTEvent(String name, String description) {
		mName = name;
		mDescription = description;
		mStart = System.currentTimeMillis() + MS_PER_DAY;
		mEnd = System.currentTimeMillis() + MS_PER_DAY + MS_PER_HOUR;
		mLocation = "Test Location";
	}

	// constructor for Parcel
	private CTEvent(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
    	mStart = in.readLong(); 	
    	mEnd = in.readLong(); 	
    	mLocation = in.readString();
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
        out.writeLong(mStart); 	
        out.writeLong(mEnd);
        out.writeString(mLocation);
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

	public String getLocation() {
		return mLocation;
	}

	public String getStartDate() {
		return getDate(mStart);
	}
	
	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @return String representing date in specified format
	 */
	private String getDate(long milliSeconds)
	{
	    // Create a DateFormatter object for displaying date in specified format.
	    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm:ss.SSS", Locale.US);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}

}
