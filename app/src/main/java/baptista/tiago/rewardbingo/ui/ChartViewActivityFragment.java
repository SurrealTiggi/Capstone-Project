package baptista.tiago.rewardbingo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import java.util.List;

import adapters.ChartViewAdapter;
import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import data.RewardsDbHelper;
import models.Rewards;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChartViewActivityFragment extends Fragment {

    private static final String TAG = ChartViewActivityFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private RecyclerView mRecyclerView;
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
        //Log.d(TAG, "onCreateView()");


        /*mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
            }
        });

        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
            }
        });*/

        this.mView = inflater.inflate(R.layout.fragment_chart_view, container, false);
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
        ChartViewAdapter adapter = new ChartViewAdapter(mContext, mRewards);
        mRecyclerView.setAdapter(adapter);

        // Work out span for proper column layout
        int span = getResources().getConfiguration().orientation;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, span + 1);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
