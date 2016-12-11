package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import baptista.tiago.rewardbingo.R;

/**
 * Created by Tiggi on 10/3/2016.
 *
 * Credit to:
 * http://stackoverflow.com/questions/2679948/focusable-edittext-inside-listview/4901683
 */
public class EditTextAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    public ArrayList myItems = new ArrayList();
    public List<String> mItems;
    private Context mContext;
    private int mTasks;

    public EditTextAdapter(Context context, int tasks) {
        this.mContext = context;
        this.mTasks = tasks;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < this.mTasks; i++) {
            ListItem listItem = new ListItem();
            listItem.caption = "Click to edit task # " + (i+1);
            myItems.add(listItem);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_new_chart_edit, null);
            holder.caption = (EditText) convertView
                    .findViewById(R.id.newTaskDescEdit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.caption.setHint(((ListItem) myItems.get(position)).caption);
        holder.caption.setId(position);
        holder.caption.setContentDescription("Click to edit single task");

        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    ((ListItem) myItems.get(position)).caption = Caption.getText().toString();
                    if (mItems == null) {
                        mItems = new ArrayList<>();
                    } else {
                        Log.d("EditTextAdapter", "Adding " + Caption.getText());
                        mItems.add(Caption.getText().toString());
                    }
                }
            }
        });
        return convertView;
    }

    public List<String> getItems() {
        return mItems;
    }

    class ListItem {
        String caption;
    }

    class ViewHolder {
        EditText caption;
    }
}
