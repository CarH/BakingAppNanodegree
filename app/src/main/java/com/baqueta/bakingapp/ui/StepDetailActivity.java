package com.baqueta.bakingapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CarH on 01/07/2017.
 */
public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnStepChangeListener {

    public final String STEPS_KEY = "mSteps";
    public final String STEP_ID_KEY = "stepId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<Step> mSteps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isPortrait = true;
        // For landscape orientation -> fullscreen
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isPortrait = false;
        }

        setContentView(R.layout.step_detail_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Get Intent data
        mSteps = getIntent().getParcelableArrayListExtra(STEPS_KEY);
        int stepId = getIntent().getIntExtra(STEP_ID_KEY, -1);

        // Set Title in Action Bar
        if (isPortrait) {
            getSupportActionBar().setTitle(mSteps.get(stepId).getShortDescription());
        } else {
            getSupportActionBar().hide();
        }

        if (savedInstanceState == null) {
            StepDetailFragment detailFragment = StepDetailFragment.instance(mSteps, stepId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, detailFragment)
                    .commit();
        }
    }

    @Override
    public void onStepChanged(int stepId) {
        if (stepId >= 0 && stepId < mSteps.size()) {
            StepDetailFragment detailFragment = StepDetailFragment.instance(mSteps, stepId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment)
                    .commit();
        }
    }
}
