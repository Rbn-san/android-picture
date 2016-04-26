package edu.cvc.uab.imagedetect.test;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import edu.cvc.uab.imagedetect.DetectorActivity;
import edu.cvc.uab.imagedetect.ImageExtractor;

public class ExtractorTest extends ActivityInstrumentationTestCase2<DetectorActivity> {
	private static final String TAG = "ExtractorTest";
	static {
		if (!OpenCVLoader.initDebug()) {
			// TODO Handle initialization error
		} else {
			Log.i(TAG, "OpenCV loaded successfully");
			/* read text_extractor */
			System.loadLibrary("image_extractor");
		}
	}

	private DetectorActivity testActivity;
	private ImageExtractor extractor;

	public ExtractorTest() {
		super(DetectorActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testActivity = getActivity();
		this.extractor = new ImageExtractor();

	}

	protected void tearDown() throws Exception {
		this.extractor = null;
		super.tearDown();
	}

	public void testUrsulaCorrect() {
		Mat image = Highgui.imread("/sdcard/cropimage.jpg");
		int number = this.extractor.detect(image, 1000.0, 10.0, 5, 18, "/sdcard/DB.matcv");
		this.assertEquals(0, number);

		/*
		 * int numMinFeatures = 5; String pathModel = "/sdcard/DB.matcv"; int
		 * numTargets = 18; String pathFiles = "/sdcard/images_db";
		 * 
		 * this.extractor.buildDatabase(numMinFeatures, numTargets, pathModel,
		 * pathFiles);
		 */
	}

}