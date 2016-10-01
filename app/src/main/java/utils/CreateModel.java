package utils;

import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Rewards;

/**
 * Created by Tiggi on 9/26/2016.
 */
public class CreateModel {

    public static final String TAG = CreateModel.class.getSimpleName();

    public static List<Rewards> createTaskList(List<String> tasksToProcess, String user) {
        List<Rewards> tasks = new ArrayList<Rewards>();

        //Log.d(TAG, "createTask(): " + tasksToProcess + "| for user: " + user);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Calendar cal = Calendar.getInstance();

        // Get start date
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, 1);
        }

        Log.d(TAG, "Will start date with " + sdf.format(cal.getTime()) + " and loop for " + tasksToProcess.size());

        for (int d = 0; d < 7; d++) {
            for (int i = 0; i < tasksToProcess.size(); i++) {
                Rewards currentRewards = new Rewards();
                currentRewards.setUser(user);
                currentRewards.setArchive(false);
                currentRewards.setDone(false);
                currentRewards.setTask(tasksToProcess.get(i));
                currentRewards.setTaskNumber(i + 1);
                currentRewards.setDay(sdf.format(cal.getTime()));
                tasks.add(currentRewards);
            }
            cal.add(Calendar.DATE, 1);
        }
        return tasks;
    }

    public static String convertDate(String dt) {
        //Log.d(TAG, "Parsing: " + dt);
        String[] parts = dt.split("-");
        int day = Integer.valueOf(parts[0]);
        int month = Integer.valueOf(parts[1]);
        int year = Integer.valueOf(parts[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day - 1);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        return String.valueOf(sdf.format(cal.getTime()));
    }
}
