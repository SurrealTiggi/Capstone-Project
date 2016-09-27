package baptista.tiago.rewardbingo.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

import models.Rewards;
import tasks.FetchTasks;
import tasks.OnTasksCompleted;
import utils.CreateModel;


/**
 * Created by Tiggi on 8/4/2016.
 */
public class NewChartActivity extends AppCompatActivity implements OnTasksCompleted {

    public static final String TAG = NewChartActivity.class.getSimpleName();
    public static final String CURRENT_TASKS = "CURRENT_TASKS";
    @Bind(R.id.taskSpinner) Spinner mTaskSpinner;
    @Bind(R.id.userEditText) EditText mUserEditText;
    @Bind(R.id.buttonClear) Button mClearButton;
    @Bind(R.id.buttonRandom) Button mRandomButton;
    @Bind(R.id.buttonSave) Button mSaveButton;
    @Bind(R.id.buttonCreate) Button mCreateButton;
    @Bind(R.id.taskListView) ListView mListView;
    public String mName;
    public String mTasks;
    private Context mContext;
    private String mRandomTasks;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chart);

        ButterKnife.bind(this);
        mRandomTasks = null;
        mItems = null;
        //mContext = this.getBaseContext();

        //TODO: Disable back button, put in warning window to return to main screen for confirmation (won't save).
        //TODO: Save bundle when user leaves the app.
        //TODO: If Bundle exists and has data, Toast and pre-load values???

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.tasks_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskSpinner.setAdapter(staticAdapter);

        mTaskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTasks = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateTaskAdapter(mTasks, 1);
            }
        });

        mRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskAdapter(mTasks, 2);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskAdapter(mTasks, 3);
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskAdapter(mTasks, 0);
            }
        });
    }

    private void updateTaskAdapter(String tasks, int edgeCase) {
        //Log.d(TAG, "updateTaskAdapter(" + edgeCase + ")");
        // Always check what we have before continuing
        mName = mUserEditText.getText().toString();
        //TODO: Always check if there's a currently incomplete chart in history for current user, complete it if there is...
        int taskTotal = Integer.parseInt(tasks);

        if (mName.matches("")) {
            Toast.makeText(this, "You have not entered a user", Toast.LENGTH_SHORT).show();
        } else {
            switch (edgeCase) {
                case 0:
                    Log.d(TAG, "Populating ListView with #" + taskTotal + " tasks...");
                    populateListView();
                    break;
                case 1:
                    Log.d(TAG, "Clearing...");
                    mUserEditText.setText("");
                    mRandomTasks = null;
                    mListView.setAdapter(null);
                    break;
                    //mTaskSpinner.requestFocus();
                case 2:
                    Log.d(TAG, "Fetching external data....");
                    new FetchTasks(this).execute("https://udacity-3-1252.appspot.com/_ah/api", mTasks);
                    break;
                case 3:
                    Log.d(TAG, "Saving and moving to view chart activity...");
                    startChartActivity();
            }
        }
    }

    private void populateListView() {
        Log.d(TAG, "populateListView()");
        mItems = new ArrayList<String>();

        if (mRandomTasks == null) {
            for (int i = 0; i < Integer.valueOf(mTasks); i++) {
                //TODO: Should be dynamic EditText
                mItems.add("Long press to edit task");
            }
        } else {
            mItems = Arrays.asList(mRandomTasks.split("\\s*: \\s*"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);

        //ListView listView = (ListView) findViewById(R.id.taskListView);
        mListView.setAdapter(adapter);
    }

    private void startChartActivity() {
        Log.d(TAG, "startChartActivity(): " + mItems);
        if (mItems != null) {
            List<Rewards> tasks = CreateModel.createTaskList(mItems, mName);
            //TODO: Pass tasks into ContentProvider to load in activity
            Intent intent = new Intent(this, ChartViewActivity.class);
            //intent.putExtra(CURRENT_TASKS, tasks);
            //this.startActivity(intent);
        } else {
            Toast.makeText(this, "Can't save as you have not created any tasks...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAPITaskCompleted(String result) {
        mRandomTasks = result;
        populateListView();
    }
}
