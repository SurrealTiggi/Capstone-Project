package widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import data.RewardsContract;
import tasks.myFetchService;

/**
 * Created by Tiggi on 10/1/2016.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    private static final String TAG = MyWidgetProvider.class.getSimpleName();

    private static Handler sWorkerQueue;
    private static HandlerThread sWorkerThread;
    private static UpdateWidgetObserver sUpdateObserver;
    private AppWidgetManager mAppWidgetManager;

    public MyWidgetProvider() {
        Log.d(TAG, "MyWidgetProvider()");

        sWorkerThread = new HandlerThread("ChartProvider-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled()");

        final ContentResolver contentResolver = context.getContentResolver();

        if(sUpdateObserver == null) {
            Log.d(TAG, "No widget observer found, creating new one...");
            try {
                mAppWidgetManager = AppWidgetManager.getInstance(context);
                sUpdateObserver = new UpdateWidgetObserver(mAppWidgetManager, context, sWorkerQueue);
                contentResolver.registerContentObserver(RewardsContract.BASE_CONTENT_URI, true, sUpdateObserver);
            } catch (Exception e) {
                Log.e(TAG, "ERROR: Unable to register ContentObserver", e);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate()");

        //Get available widgets
        ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        //Background intent to call asynctask
        //Intent intent = new Intent(context, myFetchService.class);
        //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        //Update widgets with above intent
        //context.startService(intent);
    }
}
