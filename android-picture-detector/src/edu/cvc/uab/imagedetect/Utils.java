package edu.cvc.uab.imagedetect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.util.Log;

/**
 * Created by carlos on 30/01/15.
 */
public class Utils {

	// Format: 17 width: 1920 height: 1080

	public static int WIDTH = 800;
	public static int HEIGHT = 600;

	// public static int WIDTH = 1920;
	// public static int HEIGHT = 1080;
	public static int FORMAT = 17;

	private static final String TAG = "SensorsExample::Utils::";

	public static void PrintException(Exception e) {
		if (e.getMessage() != null && e.getMessage().length() != 0) {
			Log.e(TAG, e.getMessage());
		}
		/* Error to string */
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		Log.e(TAG, sw.toString());
	}

	public static long getMemoryClass(Context context) {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass();
	}

	public static void logHeap() {
		Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
		Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
		Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		Log.d(TAG, "debug. =================================");
		Log.d(TAG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
		Log.d(TAG,
				"debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory() / 1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory() / 1048576)) + "MB ("
						+ df.format(new Double(Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
	}

	public static double GetAllocatedMemory() {
		return new Double(Runtime.getRuntime().totalMemory() / 1048576);
	}

}
