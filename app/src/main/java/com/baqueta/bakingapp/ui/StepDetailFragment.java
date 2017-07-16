package com.baqueta.bakingapp.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by CarH on 01/07/2017.
 */

public class StepDetailFragment extends Fragment {
    public static final String STEPS_KEY = "mSteps";
    public static final String STEP_ID_KEY = "stepId";

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;

    @Nullable @BindView(R.id.step_instructions)
    TextView stepInstructions;

    @Nullable @BindView(R.id.previous_next_menu)
    BottomNavigationView navigationView;

    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Step> mSteps;
    private int mStepId;

    public static StepDetailFragment instance(ArrayList<Step> steps, int stepId) {
        StepDetailFragment detailFragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_KEY, steps);
        bundle.putInt(STEP_ID_KEY, stepId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    public interface OnStepChangeListener {
        public void onStepChanged(int stepId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
        mSteps = getArguments().getParcelableArrayList(STEPS_KEY);
        mStepId = getArguments().getInt(STEP_ID_KEY);

        if (stepInstructions != null)
            stepInstructions.setText(mSteps.get(mStepId).getDescription());

        if (navigationView != null) {
            if (mStepId == 0)
                navigationView.getMenu().findItem(R.id.action_prev).setEnabled(false);

            if (mStepId == mSteps.size()-1)
                navigationView.getMenu().findItem(R.id.action_next).setEnabled(false);

            navigationView.setOnNavigationItemSelectedListener(new MenuItemSelectedListener());
        }

        initializePlayer(mSteps.get(mStepId).getVideoURL());

        return view;
    }

    private void initializePlayer(String videoURL) {
        if (mSteps == null) {
            Timber.d("mSteps is null! Could not fetch the video!");
            return;
        }
        if (videoURL.isEmpty()) {
            Timber.d("videoURL is empty. There is no video to download!");
            Toast.makeText(getContext(), "There is no video for this step", Toast.LENGTH_SHORT).show();
            return;
        }
        Timber.d("VideoURL: " + videoURL);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mExoPlayer);

        String userAgent = getUserAgent(getContext(), "BakingApp");
        DataSource.Factory factory = new DefaultDataSourceFactory(getContext(), userAgent);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL), factory, extractorsFactory, null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    /**
     * Returns a user agent string based on the given application name and the library version.
     *
     * @param context A valid context of the calling application.
     * @param applicationName String that will be prefix'ed to the generated user agent.
     * @return A user agent string generated using the applicationName and the library version.
     */
    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + "/" + versionName + " (Linux;Android " + Build.VERSION.RELEASE
                + ") " + "ExoPlayerLib/" + ExoPlayerLibraryInfo.VERSION;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    private class MenuItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_prev:
                {
                    if (mStepId > 0) {
                        ((OnStepChangeListener)getActivity()).onStepChanged(mStepId-1);
                    }
                    break;
                }
                case R.id.action_next:
                {
                    if (mStepId < mSteps.size()-1) {
                        ((OnStepChangeListener)getActivity()).onStepChanged(mStepId+1);
                    }
                    break;
                }
                default:
                    Timber.e("Menu item id unknown. Id given: " + item.getItemId());
            }
            return true;
        }
    }
}
