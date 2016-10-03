package baptista.tiago.rewardbingo.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import baptista.tiago.rewardbingo.R;

public class ChartViewActivity extends AppCompatActivity {
    private static final String TAG = ChartViewActivity.class.getSimpleName();
    private static String CONTEXT_PARENT_FLAG = "PARENT";
    public static String mParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mParent = getIntent().getStringExtra(CONTEXT_PARENT_FLAG);
        /*Bundle bundle = new Bundle();
        bundle.putString(CONTEXT_PARENT_FLAG, PARENT);

        Log.d(TAG, "Saved instance is null");
        ChartViewActivityFragment frag = new ChartViewActivityFragment();
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, frag, "STRING")
                .commit();*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
