package edu.cvc.uab.imagedetect;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class ShowSolution {
	private static final String TAG = "Loader";

	public interface TypeLoader {
		public void loadAndRun(String value);
	}

	public class ToastLoader implements TypeLoader {
		Activity activity;
		public static final String PATH_VIDEO = "PATH_VIDEO";

		public ToastLoader(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void loadAndRun(String value) {
			((DetectorActivity) this.activity).notification(value);
		}
	}

	public class VideoLoader implements TypeLoader {
		Activity activity;
		public static final String PATH_VIDEO = "PATH_VIDEO";

		public VideoLoader(Activity activity) {
			this.activity = activity;
		}

		public void loadAndRun(String value) {
			Log.i(TAG, "Loading video for value " + value);
			String path = setUpPath(value);

			Intent intent = new Intent(this.activity, VideoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(PATH_VIDEO, path);
			intent.putExtras(bundle);
			Log.i(TAG, "Starting video viewer with path " + path);
			this.activity.startActivity(intent);

		}
	}

	private String setUpPath(String value) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + value;

	}

	private String targets[];
	private Activity activity;
	private TypeLoader typeLoader;

	public ShowSolution(Activity activity, String[] targets) {
		this.activity = activity;
		this.targets = targets;
		this.typeLoader = new ToastLoader(this.activity);

	}

	public void run(int value) throws Exception {
		if (value < 0 && value >= targets.length) {
			throw new Exception("It doesn't exist image for this value");
		}
		Log.i(TAG, "Loading target value: " + value);
		typeLoader.loadAndRun(targets[value]);

	}

}
