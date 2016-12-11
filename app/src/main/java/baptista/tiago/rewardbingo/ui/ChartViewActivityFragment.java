package baptista.tiago.rewardbingo.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import adapters.ChartViewAdapter;
import baptista.tiago.rewardbingo.R;
import data.RewardsContract;
import data.RewardsDbHelper;
import models.Rewards;
import tasks.OnItemDone;
import utils.CreateModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChartViewActivityFragment extends Fragment implements OnItemDone, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ChartViewActivityFragment.class.getSimpleName();
    private static String CONTEXT_PARENT_FLAG = "PARENT";
    private Context mContext;
    private View mView;
    private ViewGroup mContainer;
    private RecyclerView mRecyclerView;
    private Button mSaveButton;
    private Button mCompleteButton;

    private List<Rewards> mRewards;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    /*
    * Loader stuff
    */
    private static String[] REWARDS_PROJECTION = {
            RewardsContract.RewardsTable.COL_ID,
            RewardsContract.RewardsTable.COL_USER,
            RewardsContract.RewardsTable.COL_DAY,
            RewardsContract.RewardsTable.COL_TASK,
            RewardsContract.RewardsTable.COL_TASK_NUMBER,
            RewardsContract.RewardsTable.COL_DONE,
            RewardsContract.RewardsTable.COL_ARCHIVED
    };

    private String[] mSelectionArgs;


    //TODO: UI: Coordinator Layout with action bar showing user name


    public ChartViewActivityFragment() {
        Log.d(TAG, "ChartViewActivityFragment()");
    }

    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);
        this.mContext = getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(true);
        //Log.d(TAG, "My parent was: " + this.getContext());
        /*Bundle bundle = this.getArguments();
        if (bundle != null) {
            String PARENT = bundle.getString(CONTEXT_PARENT_FLAG);
            Log.d(TAG, "My parent was: " + PARENT);
        } else {
            Log.d(TAG, "Bundle is null");
        }*/
        //TODO: EXTRA: Check if coming in via current chart or history chart, disable editing if history
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        if (mView == null) {
            Log.d(TAG, "View is null");
            mContainer = container;
            mView = inflater.inflate(R.layout.fragment_chart_view, container, false);
            getRewards();

        } else {
            Log.d(TAG, "View is not null");
            ((ViewGroup) mView.getParent()).removeView(mView);
        }

        mSaveButton = (Button) mView.findViewById(R.id.buttonCurrentSave);
        mCompleteButton = (Button) mView.findViewById(R.id.buttonComplete);

        // Animation test
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                saveToLocal();
            }
        });

        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                // TODO: CONTENTPROVIDER: Archive everything where isArchived = false;
                archiveAndSave();
            }
        });

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.chartRecyclerView);
        return mView;
    }

    private void getRewards() {
        //Bundle selection = new Bundle();
        //selection.putString("selectionArg","all");

        this.getActivity().getLoaderManager().initLoader(0,null,this);
    }

    private void updateDisplay() {
        Log.d(TAG, "updateDisplay(): " + mRewards);
        Log.d(TAG, "ISTRUE: " + mRewards.get(0).isDone() );
        ChartViewAdapter adapter = new ChartViewAdapter(this, mContext, mRewards);
        mRecyclerView.setAdapter(adapter);

        // Work out span for proper column layout
        int span = getResources().getConfiguration().orientation;
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, span + 1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemDone(final Rewards reward) {
        //Log.d(TAG, "Clicked: " + reward.getId() + ", " + reward.getTask());
        for (Rewards singleReward : mRewards) {
            if(singleReward.getId() == reward.getId()) {
                singleReward.setDone(true);
                Log.d(TAG, "Setting reward to true => " + singleReward.getId() + ", " + singleReward.isDone());
            }
        }
    }

    private void saveToLocal() {
        //TODO: CONTENTPROVIDER: Delete everything with given IDs and re-insert
    }

    private void archiveAndSave() {
        for (Rewards singleReward : mRewards) {
            singleReward.setArchive(true);
        }
        //TODO: CONTENTPROVIDER: Delete everything with given IDs and re-insert with archive flag
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        return new CursorLoader(
                mContext,
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
            mRewards = CreateModel.createRewardsFromCursor(data);
            }

        if (mRewards == null) {
            Toast.makeText(mContext,"No data found to populate view",Toast.LENGTH_SHORT).show();
            Intent setIntent = new Intent(mContext, MainActivity.class);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        } else {
            updateDisplay();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
    }
}
