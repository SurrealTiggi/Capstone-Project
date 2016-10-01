package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import baptista.tiago.rewardbingo.R;
import baptista.tiago.rewardbingo.ui.ChartViewActivityFragment;
import models.Rewards;
import tasks.OnItemDone;
import utils.CreateModel;

/**
 * Created by Tiggi on 8/9/2016.
 */
public class ChartViewAdapter extends RecyclerView.Adapter<ChartViewAdapter.ChartViewHolder> {

    private static final String TAG = ChartViewAdapter.class.getSimpleName();

    private OnItemDone mOnItemDone;
    private Context mContext;
    private List<Rewards> mRewardsList;

    public ChartViewAdapter(ChartViewActivityFragment chartViewActivityFragment, Context context, List<Rewards> rewards) {
        mContext = context;
        mRewardsList = rewards;
        try {
            this.mOnItemDone = ((OnItemDone) chartViewActivityFragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("ERROR: Fragment must implement OnItemDone.");
        }
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

        public void bindReward(final Rewards reward) {
            currentReward = reward;
            //TODO: Picasso to pull task images???
            mDayTextView.setText(CreateModel.convertDate(currentReward.getDay()));
            // TODO: Show only number but show info when task is clicked???
            mDescriptionTextView.setText(currentReward.getTaskNumber() + ") " + currentReward.getTask());
            mDoneCheckBox.setActivated(currentReward.isDone());
            //TODO: Set onClickListener to update main object...somehow...
            mDoneCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Inform fragment that item has been checked as DONE
                    try {
                        mOnItemDone.onItemDone(currentReward);
                    } catch (ClassCastException e) {
                        Log.e(TAG, "ERROR: Unable to callback to OnItemDone!");
                    }
                }
            });
        }
    }
}
