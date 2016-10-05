package widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.List;

import baptista.tiago.rewardbingo.R;
import data.RewardsContract;
import data.RewardsDbHelper;
import models.History;
import models.Rewards;
import utils.CreateHistory;

/**
 * Created by Tiggi on 10/2/2016.
 */
public class UpdateWidgetObserver extends ContentObserver {

    private static final String TAG = UpdateWidgetObserver.class.getSimpleName();

    private AppWidgetManager mAppWidgetManager;
    private Context mContext;
    private RemoteViews mRemoteViews;
    List<Rewards> mAllRewards;
    List<History> mHistory;

    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_SCORE = 2;

    public UpdateWidgetObserver(AppWidgetManager appWidgetManager, Context context, Handler handler) {
        super(handler);
        Log.d(TAG, "UpdateWidgetObserver()");
        mAppWidgetManager = appWidgetManager;
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.d(TAG, "onChange()");

        try {
            Cursor c = mContext.getContentResolver().query(
                    RewardsContract.BASE_CONTENT_URI, null, null, null, null);
            this.fetchData(c);
        } catch (Exception e) {
                Log.e(TAG, "ERROR: Failed to get data: " + e);
        }
        try {
            this.fetchData(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean fetchData(Cursor c) throws Exception {
        Log.d(TAG, "fetchData()");
        //TODO: CONTENTPROVIDER: Change from using dbhelper to using contentprovider properly to get all.
        mAllRewards = new RewardsDbHelper(mContext).getAllRewards();

        mHistory = CreateHistory.parseRewards(mAllRewards);
        History latestHist = mHistory.get(mHistory.size() - 1);
        this.updateWidget(latestHist);
        return true;
    }

    private void updateWidget(History latestHist) {
        Log.d(TAG, "updateWidget()");

        mRemoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.layout_chart_widget);
        ComponentName thisWidget = new ComponentName(mContext, MyWidgetProvider.class);
        int[] allWidgetIds = mAppWidgetManager.getAppWidgetIds(thisWidget);

        mRemoteViews.setTextViewText(R.id.nameWidgetView, latestHist.getUser());
        mRemoteViews.setTextViewText(R.id.scoreWidgetView, latestHist.getTotal());

        //Tell the widget manager to update widgets
        for(int widgetId: allWidgetIds) {
            mAppWidgetManager.updateAppWidget(widgetId, mRemoteViews);
        }
    }
}

