package baptista.tiago.rewardbingo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String CONTEXT_PARENT_FLAG = "PARENT";
    @Bind(R.id.newChartTextView) TextView mNewChart;
    @Bind(R.id.viewChartTextView) TextView mViewChart;
    @Bind(R.id.archiveButton) Button mArchiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mNewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewChartActivity();
            }
        });

        mViewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startViewChartActivity();
            }
        });

        mArchiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startArchiveActivity();
            }
        });

    }

    private void startNewChartActivity() {
        Log.d(TAG, "startNewChartActivity()");
        if(!existingChart()) {
            Intent intent = new Intent(this, NewChartActivity.class);
            this.startActivity(intent);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Found an existing chart, if you continue it will be archived.");
            builder1.setCancelable(false);

            builder1.setPositiveButton("Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            archiveOlder();
                            Intent intent = new Intent(getBaseContext(), NewChartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getBaseContext().startActivity(intent);
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    private void startViewChartActivity() {
        Log.d(TAG, "startViewChartActivity()");
        Intent intent = new Intent(this, ChartViewActivity.class);
        intent.putExtra(CONTEXT_PARENT_FLAG, MainActivity.class.getSimpleName());
        this.startActivity(intent);
    }

    private void startArchiveActivity() {
        Log.d(TAG, "startArchiveActivity()");
        Intent intent = new Intent(this, ArchiveActivity.class);
        this.startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void popError() {
        //AlertDialogFragment dialog = new AlertDialogFragment();
        //dialog.show(getFragmentManager(), "error_dialog");
        //Toast.makeText(this, getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
    }

    private boolean existingChart() {
        //TODO: CONTENTPROVIDER: Get count for isArchived.
        return false;
    }
    private void archiveOlder() {
        //TODO: CONTENTPROVIDER: Update everything to be archived
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
