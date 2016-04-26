package edu.cvc.uab.imagedetect.test;

import android.test.ActivityInstrumentationTestCase2;
import edu.cvc.uab.imagedetect.DetectorActivity;
import edu.cvc.uab.imagedetect.ImageExtractor;

public class BuildDatabaseClient extends ActivityInstrumentationTestCase2<DetectorActivity> {
	private static final String TAG = "PropertiesTest";
	private DetectorActivity testActivity;
	private ImageExtractor extractor;

	public BuildDatabaseClient() {
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

	public void testBuildDatabase() {
		int numMinFeatures = 5;
		String pathModel = "/sdcard/DB.matcv";
		int numTargets = 18;
		String pathFiles = "/sdcard/images_db";
		//
		// this.extractor.buildDatabase(numMinFeatures, numTargets, pathModel,
		// pathFiles);

	}

}
