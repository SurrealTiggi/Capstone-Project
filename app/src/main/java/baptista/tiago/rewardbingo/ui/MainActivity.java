package baptista.tiago.rewardbingo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import baptista.tiago.rewardbingo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
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
        Intent intent = new Intent(this, NewChartActivity.class);
        // Start intent service from here???
        this.startActivity(intent);
    }

    private void startViewChartActivity() {
        Log.d(TAG, "startViewChartActivity()");
        Intent intent = new Intent(this, ChartViewActivity.class);
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

    private void popNetworkError() {
        //AlertDialogFragment dialog = new AlertDialogFragment();
        //dialog.show(getFragmentManager(), "error_dialog");
        Toast.makeText(this, getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
    }
}
