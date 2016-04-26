package edu.cvc.uab.imagedetect;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetectorActivity extends Activity implements CvCameraViewListener2 {
	private static final String TAG = "Detector::Activity";

	enum statusPhoto {
		CLICKED, NOTCLICKED, CAPTURED
	};

	private static final long TIME_SLEEP = 3000;
	public static final String IDAUTHOR = "IDAUTHOR";
	private static final int SHOWACTIVIY = 1;
	private static statusPhoto statusCapturation = statusPhoto.NOTCLICKED;

	private Mat mRgba;
	private Mat mGray;
	private JavaCamResView mOpenCvCameraView;
	private ShowSolution showSolution;
	private Point pointIniToPaint = new Point();
	private Point pointEndToPaint = new Point();

	private double threshold = 1000;
	private double thresholdVotes = 1000;
	private int numMinFeatures;
	private String pathModel;
	private int numTargets;
	public static String[] Targets;
	private String currentTarget;
	private ImageExtractor imageExtractor;

	public DetectorActivity() {
		Log.i(TAG, "Instantiated new " + this.getClass());

	}

	public void onClickAction() {
		if (mOpenCvCameraView != null) {
			mOpenCvCameraView.autoFocus();
		}
		if (statusCapturation == statusPhoto.NOTCLICKED) {
			statusCapturation = statusPhoto.CLICKED;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.text_detect_surface_view);

		mOpenCvCameraView = (JavaCamResView) findViewById(R.id.id_activity_surface_view);
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickAction();
			}

		});

		// Get parameters
		Intent intent = getIntent();

		if (!intent.hasExtra(QuestionsActivity.NUMMINFEATS) || !intent.hasExtra(QuestionsActivity.PATHMODEL) || !intent.hasExtra(QuestionsActivity.TARGETS) || !intent.hasExtra(QuestionsActivity.THRESHOLD)
				|| !intent.hasExtra(QuestionsActivity.THRESHOLDVOTES) || !intent.hasExtra(QuestionsActivity.CURRENTTARGET)) {
			this.notification("It was not possible receive necessary parameters to start detection");
			finish();
		}

		this.numMinFeatures = intent.getIntExtra(QuestionsActivity.NUMMINFEATS, -1);
		this.pathModel = intent.getStringExtra(QuestionsActivity.PATHMODEL);
		this.Targets = intent.getStringArrayExtra(QuestionsActivity.TARGETS);
		this.threshold = intent.getDoubleExtra(QuestionsActivity.THRESHOLD, -1.0);
		this.thresholdVotes = intent.getDoubleExtra(QuestionsActivity.THRESHOLDVOTES, -1.0);
		this.currentTarget = intent.getStringExtra(QuestionsActivity.CURRENTTARGET);
		this.numTargets = this.Targets.length;

		this.imageExtractor = new ImageExtractor();
	}

	private void iniRectangle(int width, int height) {
		int originX = width / 4;
		int originY = 0;
		this.pointIniToPaint = new Point(originX, originY);
		this.pointEndToPaint = new Point(originX + (width / 2), height);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mOpenCvCameraView != null) {
			mOpenCvCameraView.enableView();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mOpenCvCameraView.disableView();
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		mGray = new Mat();
		mRgba = new Mat();
		iniRectangle(width, height);
	}

	@Override
	public void onCameraViewStopped() {
		mGray.release();
		mRgba.release();
	}

	private void startShowActivity(int idAuthor) {
		Intent intent = new Intent(this, ShowActivity.class);
		intent.putExtra(IDAUTHOR, idAuthor);
		startActivityForResult(intent, SHOWACTIVIY);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == SHOWACTIVIY) {
			this.setResult(Activity.RESULT_OK);
			finish();
		}
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Separate logic module.
		/* freeze screen */
		if (statusCapturation == statusPhoto.CLICKED) {
			Mat mBgra = new Mat();
			Imgproc.cvtColor(mRgba, mBgra, Imgproc.COLOR_RGBA2BGRA);
			Mat cropImage = new Mat(mBgra, new Rect(this.pointIniToPaint, this.pointEndToPaint));

			// int number = Arrays.binarySearch(Targets, this.currentTarget);
			// FIXME check why it is not working image extractor
			int number = -1;
			try {
				number = this.imageExtractor.detect(cropImage.clone(), threshold, thresholdVotes, numMinFeatures, numTargets, pathModel);
			} catch (Exception e) {
				Utils.PrintException(e);
			}

			Log.i(TAG, "Value detected " + number);

			// If not found return original image
			if (number == -1) {
				this.incorrect_answer_notification(getString(R.string.wrong_author));
				this.setResult(Activity.RESULT_CANCELED);
				finish();
			}
			/*
			 * if (number == -1) { statusCapturation = statusPhoto.NOTCLICKED;
			 * return mRgba; }
			 */
			/* Apply action! */
			try {
				if (Targets[number].equalsIgnoreCase(this.currentTarget)) {

					Highgui.imwrite("/sdcard/temp.png", cropImage);
					this.setResult(Activity.RESULT_OK);
					startShowActivity(number);
					finish();

				} else {
					this.incorrect_answer_notification(getString(R.string.wrong_author));
					this.setResult(Activity.RESULT_CANCELED);
					finish();
				}
			} catch (Exception e) {
				Utils.PrintException(e);
			}
			statusCapturation = statusPhoto.NOTCLICKED;
		} else if (statusCapturation == statusPhoto.CAPTURED) {
			try {
				Thread.sleep(TIME_SLEEP);
			} catch (InterruptedException e) {
				Utils.PrintException(e);
			}
			statusCapturation = statusPhoto.NOTCLICKED;
		} else {
			mRgba = inputFrame.rgba();
			// Paint rectangle
			Core.rectangle(mRgba, this.pointIniToPaint, this.pointEndToPaint, new Scalar(255, 0, 0), 3);
			mGray = inputFrame.gray();
		}
		return mRgba;
	}

	public void notification(final String value) {
		runOnUiThread(new Runnable() {
			public void run() {

				Toast toast = Toast.makeText(DetectorActivity.this, value, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	public void incorrect_answer_notification(final String value) {

		runOnUiThread(new Runnable() {
			public void run() {

				View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastLayout));

				TextView textView = (TextView) toastView.findViewById(R.id.text);
				textView.setText(value);

				ImageView bien = (ImageView) toastView.findViewById(R.id.image);
				bien.setImageResource(R.drawable.wrong);

				Toast goodtoast = new Toast(DetectorActivity.this);
				goodtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				goodtoast.setDuration(Toast.LENGTH_SHORT);
				goodtoast.setView(toastView);

				goodtoast.show();
			}
		});
	}

}
