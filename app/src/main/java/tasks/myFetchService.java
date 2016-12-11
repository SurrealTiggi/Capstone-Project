package tasks;

import android.app.IntentService;
import android.content.Intent;

import com.appspot.myapplicationid.myApi.MyApi;

import java.util.List;

import models.Rewards;

/**
 * Created by Tiggi on 10/2/2016.
 */
public class myFetchService extends IntentService {

    public static final String TAG = myFetchService.class.getSimpleName();

    public myFetchService() {
        super(myFetchService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getData();
        return;
    }

    private void getData() {
        String user;
        String score;
        //TODO: CONTENTPROVIDER: Fetch latest chart data from contentprovider

        user = "Scrooge";
        score = "24/67";
    }
}
