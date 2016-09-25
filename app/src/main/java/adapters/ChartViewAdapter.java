package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import baptista.tiago.rewardbingo.R;
import models.Rewards;

/**
 * Created by Tiggi on 8/9/2016.
 */
public class ChartViewAdapter extends RecyclerView.Adapter<ChartViewAdapter.ChartViewHolder> {

    private static final String TAG = ChartViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<Rewards> mRewardsList;

    public ChartViewAdapter(Context context, List<Rewards> rewards) {
        this.mContext = mContext;
        mRewardsList = rewards;
    }

    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chart, parent, false);
        return new ChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChartViewHolder holder, int position) {
        holder.bindReward(mRewardsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mRewardsList.size();
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder {

        public Rewards currentReward;
        public TextView mDayTextView;
        //public TextView mTaskTextView;
        public TextView mDescriptionTextView;
        public CheckBox mDoneCheckBox;

        public ChartViewHolder(View itemView) {
            super(itemView);
            mDayTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            //mTaskTextView = (TextView) itemView.findViewById(R.id.taskNumberTextView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.taskDescTextView);
            mDoneCheckBox = (CheckBox) itemView.findViewById(R.id.taskCheckbox);
        }

        public void bindReward(Rewards reward) {
            currentReward = reward;
            //TODO: Picasso to pull task images???
            mDayTextView.setText(currentReward.getDay());
            mDescriptionTextView.setText(currentReward.getTaskNumber() + ". " + currentReward.getTask());
            //mDescriptionTextView.setText(currentReward.getTask());
            //TODO: Set the checkbox depending on boolean return from currentReward.isDone()
        }
    }
}
