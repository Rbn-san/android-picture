package edu.cvc.uab.imagedetect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class QuestionLoader {
	private static final String TAG = "QuestionLoader";
	private final AssetManager assetManager;

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public QuestionLoader(Activity activity) {
		this.assetManager = activity.getResources().getAssets();
	}

	public void load(String filename, List<String> listOfLines) {
		InputStream inputStream = null;
		try {
			try {
				inputStream = assetManager.open(filename);
			} catch (IOException e) {
				Log.w(TAG, "It is not possible read the configuration from assets folder");
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + filename;
				Log.i(TAG, "Trying to read configuration from : " + path);
				inputStream = new FileInputStream(path);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "8859_1"));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine != null && !strLine.trim().isEmpty()) {
					listOfLines.add(strLine);
				}
			}

		} catch (IOException e) {
			Utils.PrintException(e);

		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception exception) {
				Log.e(TAG, "It was impossible to read the configuration file", exception.getCause());
				Utils.PrintException(exception);

			}
		}

	}

}
