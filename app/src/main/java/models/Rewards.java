package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiggi on 8/9/2016.
 */
public class Rewards implements Parcelable {

    private String mUser;
    private String mDay;
    private String mTask;
    private int mTaskNumber;
    private boolean mDone;
    private boolean mArchive;

    public Rewards() {
    }

    // Parceleable methods
    // *******************

    private Rewards (Parcel in) {
        mUser = in.readString();
        mDay = in.readString();
        mTask = in.readString();
        mTaskNumber = in.readInt();
        mDone = in.readByte() != 0;
        mArchive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUser);
        dest.writeString(mDay);
        dest.writeString(mTask);
        dest.writeInt(mTaskNumber);
        dest.writeByte((byte) (mDone ? 1 : 0));
        dest.writeByte((byte) (mArchive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Rewards> CREATOR
            = new Parcelable.Creator<Rewards>() {

        @Override
        public Rewards createFromParcel(Parcel in) {
            return new Rewards(in);
        }

        @Override
        public Rewards[] newArray(int size) {
            return new Rewards[size];
        }
    };

    // Getters and Setters
    // *******************

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getTask() {
        return mTask;
    }

    public void setTask(String task) {
        mTask = task;
    }

    public int getTaskNumber() {
        return mTaskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        mTaskNumber = taskNumber;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public boolean isArchive() {
        return mArchive;
    }

    public void setArchive(boolean archive) {
        mArchive = archive;
    }

}
