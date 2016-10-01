package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import baptista.tiago.rewardbingo.R;
import models.History;

/**
 * Created by Tiggi on 10/1/2016.
 */
public class ArchiveViewAdapter extends RecyclerView.Adapter<ArchiveViewAdapter.ArchiveViewHolder> {
    private static final String TAG = ArchiveViewAdapter.class.getSimpleName();
    private Context mContext;
    private List<History> mRewardsList;

    public ArchiveViewAdapter(Context context, List<History> rewardsList) {
        this.mContext = context;
        this.mRewardsList = rewardsList;
    }

    public ArchiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_archive_chart, parent, false);
        return new ArchiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArchiveViewAdapter.ArchiveViewHolder holder, int position) {
        holder.bindHistory(mRewardsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mRewardsList.size();
    }

    public class ArchiveViewHolder extends RecyclerView.ViewHolder {

        public History currentHistory;
        public TextView nameHist;
        public TextView dateHist;
        public TextView totalHist;

        public ArchiveViewHolder(View itemView) {
            super(itemView);
            nameHist = (TextView) itemView.findViewById(R.id.nameArcTextView);
            dateHist = (TextView) itemView.findViewById(R.id.dateArcTextView);
            totalHist = (TextView) itemView.findViewById(R.id.totalArcTextView);
        }

        public void bindHistory(final History history) {
            currentHistory = history;
            nameHist.setText(history.getUser());
            dateHist.setText(history.getStartDay());
            totalHist.setText(history.getTotal());
            //TODO: Set delete onClick???
        }
    }

}