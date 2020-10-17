package com.example.simplevideoview;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "tacoma_narrows";
    private VideoView mVideoView;


    // The playback position is recorded in milliseconds from 0.
    private int mCurrentPosition = 0;

    // Add a member variable to hold the key for the playback position in the instance state bundle
    private static final String PLAYBACK_TIME = "play_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoview);

        // check for the existence of the instance state bundle,
        // and update the value of mCurrentTime with the value from that bundle.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }  // Add these lines before you create the MediaController.

        // create a new MediaController object
        MediaController controller = new MediaController(this);

        // use setMediaPlayer() to connect the object to the VideoView
        controller.setMediaPlayer(mVideoView);

        // Use setMediaController() to do the reverse connection, that is, to tell the VideoView that
        // the MediaController will be used to control it:
        mVideoView.setMediaController(controller);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition()); // Get the current playback position with the getCurrentPosition() method.
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // This test is required because the behavior of onPause() and onStop() changed in Android N (7.0, API 24).
        // In older versions of Android, onPause() was the end of the visual lifecycle of your app,
        // and you could start releasing resources when the app was paused.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() + "/raw/" + mediaName);
    }

    private void initializePlayer() {

        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);  // set the media URI that the VideoView will play.

        // Check to see whether the current position is greater than 0,
        // which indicates that the video was playing at some point.
        if (mCurrentPosition > 0) {
            mVideoView.seekTo(mCurrentPosition);
        } else {

            // If the current position is 0, the video has not yet played.
            // Use seekTo() to set the playback position to 1 millisecond.
            // This will show the first frame of the video rather than a black screen.
            mVideoView.seekTo(1);  // Use the seekTo() method to move the playback position to the current position.
        }

        mVideoView.start();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this, "Playback completed",Toast.LENGTH_SHORT).show();

                // reset the playback and the MediaController to the beginning of the clip
                // so that the video can be played again.
                mVideoView.seekTo(1);
            }
        });

    }

    // This stops the video from playing and releases all the resources held by the VideoView.
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }
}