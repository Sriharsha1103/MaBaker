package com.example.aammu.mabaker;

import android.annotation.TargetApi;
import android.app.PictureInPictureParams;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aammu.mabaker.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class Fragment_Display extends Fragment{

    public static final String FRAG_PREF = "frag_pref";
    @BindView(R.id.id_exoPlayer_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.id_steps_detail_description)
    TextView description;
    SimpleExoPlayer exoPlayer;
    private Steps steps;
    private long position=0;
    private String videoURL =null;
    private boolean playWhenReady=true;
    SharedPreferences sharedPreferences;



    public Fragment_Display() {
    }

    public static Fragment_Display newInstance(Steps steps){
        Bundle args = new Bundle();
        args.putParcelable("steps",steps);
        Fragment_Display fragment_display = new Fragment_Display();
        fragment_display.setArguments(args);
        return fragment_display;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_steps_detail,container,false);
        ButterKnife.bind(this,v);
        Bundle bundle = this.getArguments();
        steps = bundle.getParcelable("steps");description.setText(steps.getDescription());
        startPlayer();
        if(savedInstanceState!=null && exoPlayer!=null){
            steps = savedInstanceState.getParcelable("SavedSteps");
            position = savedInstanceState.getLong(getResources().getString(R.string.selected_position));
            exoPlayer.seekTo(position);
            playWhenReady=savedInstanceState.getBoolean(getString(R.string.playWhenReady));
            exoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(getString(R.string.playWhenReady)));
        }
        return v;
    }

    private void startPlayer() {
        if (steps.getThumbNail_URL().equals("") && steps.getVideo_URL().equals("")) {
            exoPlayerView.setVisibility(GONE);

        }
        else if (steps.getThumbNail_URL().equals("")) {
            videoURL = steps.getVideo_URL();
            setExoPlayer();
        }
        else {
            videoURL = steps.getThumbNail_URL();
            setExoPlayer();
        }

    }

    private void setExoPlayer() {
        if(videoURL!=null && exoPlayer==null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            Uri uri = Uri.parse(videoURL);
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(
                    uri, defaultHttpDataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.seekTo(position);
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SavedSteps", steps);
        if (exoPlayer != null) {
            outState.putLong(getResources().getString(R.string.selected_position), exoPlayer.getCurrentPosition());
            outState.putBoolean(getString(R.string.playWhenReady), exoPlayer.getPlayWhenReady());
            position = exoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences = getActivity().getSharedPreferences(FRAG_PREF,0);
        if (exoPlayer != null) {
            position = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("currentPosition",position);
            editor.putBoolean("playWhenReady",playWhenReady);
            editor.apply();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(FRAG_PREF,0);
        startPlayer();
        if(exoPlayer!=null){
            exoPlayer.seekTo(sharedPreferences1.getLong("currentPosition",0));
            exoPlayer.setPlayWhenReady(sharedPreferences1.getBoolean("playWhenReady",false));
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer=null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        final PictureInPictureParams.Builder mPictureInPictureParamsBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();

            if (!isInPictureInPictureMode) {
                if (exoPlayerView != null) {
                    exoPlayerView.showController();

                } else {
                    exoPlayerView.hideController();
                    Rational aspectRatio = new Rational(exoPlayerView.getWidth(), exoPlayerView.getHeight());
                    mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                    getActivity().enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
                }
            }
        }

    }
}
