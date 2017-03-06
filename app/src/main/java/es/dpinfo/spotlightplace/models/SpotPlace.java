package es.dpinfo.spotlightplace.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dprimenko on 10/11/16.
 */
public class SpotPlace implements Parcelable {

    private String mId;
    private String mCreatorId;
    private String mTitle;
    private String mImg;
    private String mAddress;
    private String mDescription;
    private String mCategory;
    private String mDateTimeFrom;
    private String mDateTimeTo;
    private List<String> mUsersIn;
    private int mUsersInInt;


    protected SpotPlace(Parcel in) {
        mId = in.readString();
        mCreatorId = in.readString();
        mTitle = in.readString();
        mImg = in.readString();
        mAddress = in.readString();
        mDescription = in.readString();
        mCategory = in.readString();
        mDateTimeFrom = in.readString();
        mDateTimeTo = in.readString();
        mUsersIn = in.createStringArrayList();
        mUsersInInt = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mCreatorId);
        dest.writeString(mTitle);
        dest.writeString(mImg);
        dest.writeString(mAddress);
        dest.writeString(mDescription);
        dest.writeString(mCategory);
        dest.writeString(mDateTimeFrom);
        dest.writeString(mDateTimeTo);
        dest.writeStringList(mUsersIn);
        dest.writeInt(mUsersInInt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpotPlace> CREATOR = new Creator<SpotPlace>() {
        @Override
        public SpotPlace createFromParcel(Parcel in) {
            return new SpotPlace(in);
        }

        @Override
        public SpotPlace[] newArray(int size) {
            return new SpotPlace[size];
        }
    };

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmDateTimeFrom() {
        return mDateTimeFrom;
    }

    public void setmDateTimeFrom(String mDateTimeFrom) {
        this.mDateTimeFrom = mDateTimeFrom;
    }

    public String getmDateTimeTo() {
        return mDateTimeTo;
    }

    public void setmDateTimeTo(String mDateTimeTo) {
        this.mDateTimeTo = mDateTimeTo;
    }

    public String getmCreatorId() {
        return mCreatorId;
    }

    public void setmCreatorId(String mCreatorId) {
        this.mCreatorId = mCreatorId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public List<String> getmUsersIn() {
        return mUsersIn;
    }

    public void setmUsersIn(List<String> mUsersIn) {
        this.mUsersIn = mUsersIn;
    }

    public int getmUsersInInt() {
        return mUsersInInt;
    }

    public void setmUsersInInt(int mUsersInInt) {
        this.mUsersInInt = mUsersInInt;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof SpotPlace) {

            SpotPlace spotPlace = (SpotPlace) o;

            if (spotPlace.getmId() == this.getmId()) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public String toString() {

        return "{" +
                "\"creator\" : \"" + mCreatorId + "\"" +
                ", \"title\" : \"" + mTitle + "\"" +
                ", \"img\" : \"" + mImg + "\"" +
                ", \"address\" : \"" + mAddress + "\"" +
                ", \"description\" : \"" + mDescription + "\"" +
                ", \"category\" : \"" + mCategory + "\"" +
                ", \"datetime_from\" : \"" + mDateTimeFrom + "\"" +
                ", \"datetime_to\" : \"" + mDateTimeTo + "\"" +
                ", \"users_in\" : [" + getPeopleToString() + "]" +
                "}";
    }

    private String getPeopleToString() {
        String result = "";

        for (int i = 0; i < mUsersIn.size(); i++) {
            if (i != (mUsersIn.size() - 1)) {
                result += ("\"" + mUsersIn.get(i) + "\"" + ",");
            } else {
                result += ("\"" + mUsersIn.get(i) + "\"");
            }
        }

        return result;
    }

    public SpotPlace() {

    }

    public SpotPlace(String id, String creatorId, String title, String img, String address, String description, String category, String datetimeFrom, String datetimeTo, List<String> usersIn) {

        this.mId = id;
        this.mCreatorId = creatorId;
        this.mTitle = title;
        this.mImg = img;
        this.mAddress = address;
        this.mDescription = description;
        this.mCategory = category;
        this.mDateTimeFrom = datetimeFrom;
        this.mDateTimeTo = datetimeTo;
        this.mUsersIn = usersIn;
    }

    public SpotPlace(String id, String creatorId, String title, String img, String address, String description, String category, String datetimeFrom, String datetimeTo, int usersIn) {

        this.mId = id;
        this.mCreatorId = creatorId;
        this.mTitle = title;
        this.mImg = img;
        this.mAddress = address;
        this.mDescription = description;
        this.mCategory = category;
        this.mDateTimeFrom = datetimeFrom;
        this.mDateTimeTo = datetimeTo;
        this.mUsersInInt = usersIn;
    }
}
