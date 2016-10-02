package utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.History;
import models.Rewards;

/**
 * Created by Tiggi on 10/1/2016.
 */
public class CreateHistory {

    public static final String TAG = CreateHistory.class.getSimpleName();

    public static List<History> parseRewards(List<Rewards> fullRewards) {
        List<History> mHistory = new ArrayList<>();

        HashMap<String, String> mHistoryHash = new HashMap<>();
        String startDay = "";
        int score = 0;
        int total = 0;

        for (int i = 0; i < fullRewards.size(); i++) {
            Rewards currentReward = fullRewards.get(i);

            if (mHistoryHash.get(currentReward.getUser()) == null) {
                // Assume it's a new user
                mHistoryHash.put(currentReward.getUser(), "0");
                total = 1;
                score = 0;
            } else {
                total++;
            }
            if (CreateModel.convertDate(currentReward.getDay()).equals("Mon")) {
                // Only set the date if it's a Monday
                startDay = currentReward.getDay();
            }
            if (currentReward.isDone()) {
                // Add to score
                score++;
            }
            //Log.d(TAG, "Processed user: " + currentReward.getUser() + ", total: " + total);

            mHistoryHash.put(
                    currentReward.getUser(),
                    startDay +", " + score + "/" + total);
        }

        Iterator it = mHistoryHash.entrySet().iterator();
        while (it.hasNext()) {
            History hist = new History();
            Map.Entry pair = (Map.Entry) it.next();
            String[] parts = pair.getValue().toString().split(", ");

            hist.setUser(pair.getKey().toString());
            hist.setStartDay(parts[0]);
            hist.setTotal(parts[1]);
            mHistory.add(hist);
            it.remove();
        }

        return mHistory;
    }
}
