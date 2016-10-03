package baptista.tiago.rewardbingo.ui;

import android.app.ListActivity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

import data.RewardsContract;
import data.RewardsDbHelper;
import data.RewardsProvider;
import models.Rewards;
import tasks.FetchTasks;
import tasks.OnTasksCompleted;
import utils.CreateModel;


/**
 * Created by Tiggi on 8/4/2016.
 */
public class NewChartActivity extends AppCompatActivity implements OnTasksCompleted {

    public static final String TAG = NewChartActivity.class.getSimpleName();
    public static final String PARENT = "PARENT";
    @Bind(R.id.taskSpinner) Spinner mTaskSpinner;
    @Bind(R.id.userEditText) EditText mUserEditText;
    @Bind(R.id.buttonClear) Button mClearButton;
    @Bind(R.id.buttonRandom) Button mRandomButton;
    @Bind(R.id.buttonSave) Button mSaveButton;
    @Bind(R.id.buttonCreate) Button mCreateButton;
    @Bind(R.id.taskListView) ListView mListView;
    public String mName;
    public String mTasks;
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
                clearAll();
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
                    mUserEditText.setText("");
                    mRandomTasks = null;
                    mListView.setAdapter(null);
                    break;
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
                //TODO: UI: Should be dynamic EditText
                mItems.add("Long press to edit task");
            }
        } else {
            mItems = Arrays.asList(mRandomTasks.split("\\s*: \\s*"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(adapter);
    }

    private void startChartActivity() {
        Log.d(TAG, "startChartActivity(): " + mItems);
        if (mItems != null) {
            List<Rewards> tasks = CreateModel.createTaskList(mItems, mName);

            // Loop through task list and add into local store using ContentProvider
            ArrayList<ContentProviderOperation> cpo = new ArrayList<>();
            Uri dirUri = RewardsContract.RewardsTable.buildSingleUri();

            try {
                for (int i = 0; i < tasks.size(); i++) {
                    ContentValues values = new ContentValues();
                    Rewards currentTask = tasks.get(i);

                    values.put(RewardsContract.RewardsTable.COL_USER, currentTask.getUser());
                    values.put(RewardsContract.RewardsTable.COL_ARCHIVED, currentTask.isArchive());
                    values.put(RewardsContract.RewardsTable.COL_DONE, currentTask.isDone());
                    values.put(RewardsContract.RewardsTable.COL_TASK, currentTask.getTask());
                    values.put(RewardsContract.RewardsTable.COL_TASK_NUMBER, currentTask.getTaskNumber());
                    values.put(RewardsContract.RewardsTable.COL_DAY, currentTask.getDay());

                    cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
                }

                getContentResolver().applyBatch(RewardsContract.CONTENT_AUTHORITY, cpo);

            } catch (RemoteException | OperationApplicationException | UnsupportedOperationException e) {
                Log.e(TAG, "Error updating content.", e);
            }
            // Quick test to check if we inserted correctly
            //testRetrieve();

            // Start the next activity...finally...
            Intent intent = new Intent(this, ChartViewActivity.class);
            intent.putExtra(PARENT, MainActivity.class.getSimpleName());
            this.startActivity(intent);
        } else {
            Toast.makeText(this, "Can't save as you have not created any tasks...", Toast.LENGTH_SHORT).show();
        }
    }

    /*public void testRetrieve() {
        List<Rewards> retList = new RewardsDbHelper(this).getAllRewards();
        Log.d(TAG, "Fetched from db: " + retList);
    }*/

    public void clearAll() {
        new RewardsDbHelper(this).deleteAllData();
    }

    @Override
    public void onAPITaskCompleted(String result) {
        mRandomTasks = result;
        populateListView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d(TAG, "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Warning, exiting here will not save your chart. Are you sure?");
        builder1.setCancelable(false);

        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent setIntent = new Intent(getBaseContext(), MainActivity.class);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
