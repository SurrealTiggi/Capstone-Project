package baptista.tiago.rewardbingo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import adapters.ArchiveViewAdapter;
import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import data.RewardsDbHelper;
import models.History;
import models.Rewards;
import utils.CreateHistory;

/**
 * Created by Tiggi on 10/1/2016.
 */
public class ArchiveActivity extends AppCompatActivity {

    private final String TAG = ArchiveActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    List<Rewards> mHistRewards;
    List<History> mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_view);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.historyRecyclerView);
        getHistory();
        updateDisplay();
    }

    private void getHistory() {
        mHistRewards = new RewardsDbHelper(this).getAllRewards();
        mHistory = CreateHistory.parseRewards(mHistRewards);
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

}
