package edu.cvc.uab.imagedetect.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import edu.cvc.uab.imagedetect.DetectorActivity;
import edu.cvc.uab.imagedetect.Properties;

public class PropertiesTest extends ActivityInstrumentationTestCase2<DetectorActivity> {

	private static final String TAG = "PropertiesTest";
	private DetectorActivity testActivity;
	private Properties properties;

	public PropertiesTest() {
		super(DetectorActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testActivity = getActivity();

	}

	protected void tearDown() throws Exception {
		this.properties = null;
		super.tearDown();

	}

	public void testProperties() {
		try {
			Log.i(TAG, "Starting first properties test");
			this.properties = new Properties(testActivity);
		} catch (Exception exception) {
			fail(exception.getMessage());
			exception.getCause();
		}
	}

	public void testLoadThreshold() {
		try {
			Log.i(TAG, "Starting Load Threshold properties test");
			this.properties = new Properties(testActivity);
			assertNotNull(this.properties.getThreshold());
			assertEquals(this.properties.getThreshold(), 1000.0);
		} catch (Exception exception) {
			fail(exception.getMessage());
			exception.getCause();
		}

	}

	public void testLoadThresholdVotes() {
		try {
			Log.i(TAG, "Starting Load Threshold properties test");
			this.properties = new Properties(testActivity);
			assertNotNull(this.properties.getThresholdVotes());
			assertEquals(this.properties.getThresholdVotes(), 10.0);
		} catch (Exception exception) {
			fail(exception.getMessage());
			exception.getCause();
		}
	}

	public void testPathModel() {
		try {
			Log.i(TAG, "Starting Load Threshold properties test");
			this.properties = new Properties(testActivity);
			assertNotNull(this.properties.getPathModel());
			assertEquals(this.properties.getPathModel(), "/sdcard/DB.matcv");
		} catch (Exception exception) {
			fail(exception.getMessage());
			exception.getCause();
		}
	}

	public void testNumMinFeatures() {
		try {
			Log.i(TAG, "Starting Load Threshold properties test");
			this.properties = new Properties(testActivity);
			assertNotNull(this.properties.getNumMinFeatures());
			assertEquals(this.properties.getNumMinFeatures(), 5);
		} catch (Exception exception) {
			fail(exception.getMessage());
			exception.getCause();
		}
	}

	public void testTargets() {
		try {
			Log.i(TAG, "Starting Load Threshold properties test");
			this.properties = new Properties(testActivity);
			assertNotNull(this.properties.getTargets());
			;
		} catch (Exception exception) {
			fail(exception.getMessage());
			exception.getCause();
		}
	}
}