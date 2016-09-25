package baptista.tiago.rewardbingo.ui;

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

import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

import tasks.FetchTasks;
import tasks.OnTasksCompleted;


/**
 * Created by Tiggi on 8/4/2016.
 */
public class NewChartActivity extends AppCompatActivity implements OnTasksCompleted {

    public static final String TAG = NewChartActivity.class.getSimpleName();
    @Bind(R.id.taskSpinner) Spinner mTaskSpinner;
    @Bind(R.id.userEditText) EditText mUserEditText;
    @Bind(R.id.buttonClear) Button mClearButton;
    @Bind(R.id.buttonRandom) Button mRandomButton;
    @Bind(R.id.buttonSave) Button mSaveButton;
    @Bind(R.id.taskListView) ListView mTaskListView;
    @Bind(R.id.buttonCreate) Button mCreateButton;
    public String mName;
    public String mTasks;
    private Context mContext;
    private String mRandomTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chart);

        ButterKnife.bind(this);
        mContext = this.getBaseContext();

        // Remove views in case user tries to click.
        //mGridLayout.removeAllViews();

        //TODO: Disable back button, put in warning window to return to main screen for confirmation (won't save).
        //TODO: Save bundle when user leaves the app.
        //TODO: If Bundle exists and has data, Toast and pre-load values???
        //TODO: Always check if there's a currently incomplete chart in history for current user, complete it if there is...

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
                //TODO: Re-create tasks grid with existing elements.
                updateTaskAdapter(mTasks, 1);
            }
        });

        mRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskAdapter(mTasks, 2);
                //TODO: Populate fields with values from API call.
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Save everything into db
                //TODO: Load up chart view Activity
                startChartActivity();
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskAdapter(mTasks, 0);
                //TODO: Populate fields with values from API call.
            }
        });

        //updateTaskAdapter(mTasks, 0);

    }

    private void updateTaskAdapter(String tasks, int edgeCase) {
        Log.d(TAG, "updateTaskAdapter(" + edgeCase + ")");
        // Always check what we have before continuing
        mName = mUserEditText.getText().toString();
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
                    //TODO: Clear List view if exists, clear name, clear tasks
                    break;
                    //mTaskSpinner.requestFocus();
                case 2:
                    Log.d(TAG, "Fetching external data....");
                    new FetchTasks(this).execute("https://udacity-3-1252.appspot.com/_ah/api", mTasks);
                    break;
            }
        }
    }

    private void populateListView() {
        Log.d(TAG, "populateListView()");
        if (mRandomTasks == null ) {
            //Random was not pressed so populating as normal.
        } else {
            //Random was pressed, so setting all views with string
        }

    }
        //mGridLayout.setColumnCount(taskPerRow);
        //mGridLayout.setRowCount(rowTotal + 1);
/*
        for (int i = 0, c = 0, r = 0; i < rowTotal; i++, c++) {
            if (c == taskPerRow) {
                c = 0;
                r++;
            }
            TextView taskNum= new TextView(this);
            Log.d(TAG, "Doing #" + i);
            taskNum.setText("#" + (i + 1));
            //taskNum.setTextSize(R.dimen.button_regular_text_size);

            //oImageView.setLayoutParams(new GridLayout.LayoutParams(100, 100));

            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            if (r == 0 && c == 0) {
                //Log.e("", "spec");
                colspan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2);
            }
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            mGridLayout.addView(taskNum, gridParam);
        }*/

    private void startChartActivity() {
        Log.d(TAG, "startChartActivity()");
        //Intent intent = new Intent(this, ChartViewActivity.class);
        //this.startActivity(intent);
    }

    @Override
    public void onAPITaskCompleted(String result) {
        //TODO: Implement util to build some sort of object from the result string...
        mRandomTasks = result;
        Log.d(TAG, "Got: " + mRandomTasks);
        populateListView();
    }
}
