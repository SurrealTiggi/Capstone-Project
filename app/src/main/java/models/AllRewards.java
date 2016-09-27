package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Tiggi on 9/26/2016.
 */
public class AllRewards{

    private List<Rewards> mRewards;

    public List<Rewards> getRewards() {
        return mRewards;
    }

    public void setRewards(List<Rewards> rewards) {
        mRewards = rewards;
    }

}
