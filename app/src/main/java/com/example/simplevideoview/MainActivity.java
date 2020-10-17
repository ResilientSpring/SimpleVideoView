package com.example.simplevideoview;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "tacoma_narrows";
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoview);
    }

    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() + "/raw/" + mediaName);
    }

    private void initializePlayer() {

        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);  // set the media URI that the VideoView will play.
        mVideoView.start();

    }
}