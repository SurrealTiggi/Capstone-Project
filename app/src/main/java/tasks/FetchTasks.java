package tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.myapplicationid.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by Tiggi on 9/19/2016.
 */
public class FetchTasks extends AsyncTask<String, Void, String> {

    //TODO: Update to Intent Service (Check football scores)

    private final static String TAG = FetchTasks.class.getSimpleName();
    private static MyApi myApiService = null;

    private OnTasksCompleted delegate;

    public FetchTasks(OnTasksCompleted delegate) {
        Log.d(TAG, "FetchTasks()");
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "Initiating AsyncTask with url " + params[0] + " and #" + params[1]);
        if (myApiService == null) {
            Log.d(TAG, "Building myApiService...");
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
            .setRootUrl(params[0]);

            myApiService = builder.build();
        }

        String result;
        try {
            Log.d(TAG, "Trying to get tasks...");
            int numTasks = Integer.valueOf(params[1]);
            return myApiService.fetchTasks(numTasks).execute().getData();
        } catch (IOException e) {
            Log.d(TAG, "EXCEPTION: " + e.getMessage());
            result = e.getMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (this.delegate != null) {
            this.delegate.onAPITaskCompleted(result);
        } else {
            Log.d(TAG, "ERROR: Delegate is NULL!");
        }
    }
}
