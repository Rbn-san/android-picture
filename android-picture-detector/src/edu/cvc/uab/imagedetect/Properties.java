package edu.cvc.uab.imagedetect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class Properties {
	private static final String TAG = "Properties";
	private static final String FILENAME = "picturedetector.properties";
	java.util.Properties properties;

	public Properties(Activity activity) throws IOException {
		InputStream inputStream = null;
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FILENAME;

			Log.i(TAG, "Trying to read configuration from : " + path);
			inputStream = new FileInputStream(path);

			properties = new java.util.Properties();
			properties.load(inputStream);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception exception) {
				Log.i(TAG, "It was impossible to read the configuration file", exception.getCause());
			}
		}
	}

	public double getThreshold() {
		return Double.valueOf((properties.getProperty("threshold")));
	}

	public double getThresholdVotes() {
		return Double.valueOf((properties.getProperty("threshold_votes")));
	}

	public String[] getTargets() {
		return properties.getProperty("targets").split(",");
	}

	public String getPathModel() {
		return properties.getProperty("path_model");
	}

	public int getNumMinFeatures() {
		return Integer.valueOf(properties.getProperty("num_min_features"));
	}

	public String getLinkTarget(Integer numTarget) {
		return properties.getProperty("target" + numTarget.toString());
	}

}
