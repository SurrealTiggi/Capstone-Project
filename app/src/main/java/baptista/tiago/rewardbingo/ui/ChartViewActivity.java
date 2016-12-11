package baptista.tiago.rewardbingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import baptista.tiago.rewardbingo.R;

public class ChartViewActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private static final String TAG = ChartViewActivity.class.getSimpleName();
    private static String CONTEXT_PARENT_FLAG = "PARENT";
    private static String FRAGMENT_TAG = "CHART_VIEW_FRAGMENT";
    public static String mParent;

    // AppBar Layout stuff
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_container);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_bar);

        mParent = getIntent().getStringExtra(CONTEXT_PARENT_FLAG);
        Bundle bundle = new Bundle();
        bundle.putString(CONTEXT_PARENT_FLAG, mParent);

        try {
            ChartViewActivityFragment frag = (ChartViewActivityFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        } catch (Exception e) {
            Log.e(TAG, "ERROR: " + e);
        }

        /*ChartViewActivityFragment frag = new ChartViewActivityFragment();
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, frag, FRAGMENT_TAG)
                .commit();*/
        /*ChartViewActivityFragment frag = (ChartViewActivityFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (frag == null) {
            frag = new ChartViewActivityFragment();
            frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, frag, FRAGMENT_TAG)
                    .commit();
        } else {
            frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, frag, FRAGMENT_TAG)
                    .commit();
        }*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        Intent setIntent = new Intent(getBaseContext(), MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }
}
