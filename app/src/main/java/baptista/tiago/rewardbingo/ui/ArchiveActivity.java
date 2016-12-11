package baptista.tiago.rewardbingo.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import adapters.ArchiveViewAdapter;
import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import data.RewardsContract;
import data.RewardsDbHelper;
import models.History;
import models.Rewards;
import utils.CreateHistory;
import utils.CreateModel;

/**
 * Created by Tiggi on 10/1/2016.
 */
public class ArchiveActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = ArchiveActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    List<Rewards> mHistRewards;
    List<History> mHistory;

    private static String[] REWARDS_PROJECTION = {
            RewardsContract.RewardsTable.COL_ID,
            RewardsContract.RewardsTable.COL_USER,
            RewardsContract.RewardsTable.COL_DAY,
            RewardsContract.RewardsTable.COL_TASK,
            RewardsContract.RewardsTable.COL_TASK_NUMBER,
            RewardsContract.RewardsTable.COL_DONE,
            RewardsContract.RewardsTable.COL_ARCHIVED
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_view);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.historyRecyclerView);
        getHistory();
        //updateDisplay();
    }

    private void getHistory() {
        getLoaderManager().initLoader(0,null,this);
        //mHistRewards = new RewardsDbHelper(this).getAllRewards();
        //mHistory = CreateHistory.parseRewards(mHistRewards);
    }

    private void updateDisplay() {

        Log.d(TAG, "updateDisplay()");
        ArchiveViewAdapter adapter = new ArchiveViewAdapter(this, mHistory);
        mRecyclerView.setAdapter(adapter);

        int span = getResources().getConfiguration().orientation;
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, span + 1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        return new CursorLoader(
                this,
                RewardsContract.BASE_CONTENT_URI,
                REWARDS_PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished()");
        if (data.getCount() > 0) {
            mHistRewards = CreateModel.createRewardsFromCursor(data);
        }

        if (mHistRewards == null) {
            Toast.makeText(this,"No data found to populate view",Toast.LENGTH_SHORT).show();
            Intent setIntent = new Intent(this, MainActivity.class);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        } else {
            mHistory = CreateHistory.parseRewards(mHistRewards);
            updateDisplay();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
    }
}
