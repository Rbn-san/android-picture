package edu.cvc.uab.imagedetect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	private static final String TAG = "Detector::VideoActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Starting video viewer...");
		setContentView(R.layout.video_player);
		VideoView myVideoView = (VideoView) findViewById(R.id.video_view);

		Log.i(TAG, "Getting parameters for video...");
		Intent intent = getIntent();

		if (intent == null || intent.getExtras() == null) {
			Utils.PrintException((new Exception("It was not possible to read video parameters")));
			return;
		}
		Log.i(TAG, "Getting string for video...");
		String path = intent.getExtras().getString(ShowSolution.VideoLoader.PATH_VIDEO);
		Log.i(TAG, "Loading video from path " + path);
		myVideoView.setVideoURI(Uri.parse(path));
		myVideoView.setMediaController(new MediaController(this));
		myVideoView.requestFocus();
		myVideoView.start();
	}
}