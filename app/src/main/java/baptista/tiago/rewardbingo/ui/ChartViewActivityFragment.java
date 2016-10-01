package baptista.tiago.rewardbingo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.google.common.base.Predicate;

import java.util.List;

import adapters.ChartViewAdapter;
import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import data.RewardsDbHelper;
import models.Rewards;
import tasks.OnItemDone;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChartViewActivityFragment extends Fragment implements OnItemDone {

    private static final String TAG = ChartViewActivityFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private ViewGroup mContainer;
    private RecyclerView mRecyclerView;
    private Button mSaveButton;
    private Button mCompleteButton;

    private List<Rewards> mRewards;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    public ChartViewActivityFragment() {
        Log.d(TAG, "ChartViewActivityFragment()");
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        this.mContext = getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(true);
        //TODO: Check if coming in via current chart or history chart, disable editing if history
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        this.mContainer = container;
        this.mView = inflater.inflate(R.layout.fragment_chart_view, container, false);
        mSaveButton = (Button) mView.findViewById(R.id.buttonCurrentSave);
        mCompleteButton = (Button) mView.findViewById(R.id.buttonComplete);

        // Animation test
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                // TODO: Save entire object to ContentProvider
                saveToLocal();
            }
        });

        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                // TODO: Implement archival method
                archiveAndSave();
            }
        });

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.chartRecyclerView);
        getRewards();
        updateDisplay();
        return mView;
    }

    private void getRewards() {
        //TODO: Build data model to populate view, ie. fetch from local db with contentprovider
        mRewards = new RewardsDbHelper(this.mContext).getAllRewards();
    }

    private void updateDisplay() {
        Log.d(TAG, "updateDisplay()");
        ChartViewAdapter adapter = new ChartViewAdapter(this, this.mContext, mRewards);
        mRecyclerView.setAdapter(adapter);

        // Work out span for proper column layout
        int span = getResources().getConfiguration().orientation;
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, span + 1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemDone(final Rewards reward) {
        Log.d(TAG, "Clicked: " + reward.getId() + ", " + reward.getTask());
        for (Rewards singleReward : mRewards) {
            if(singleReward.getId() == reward.getId()) {
                singleReward.setDone(true);
            }
        }
    }

    private void saveToLocal() {
        new RewardsDbHelper(mContext).updateRewards(mRewards);
    }

    private void archiveAndSave() {
        for (Rewards singleReward : mRewards) {
            singleReward.setArchive(true);
        }
        new RewardsDbHelper(mContext).archiveRewards(mRewards);
    }
}
