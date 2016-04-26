package edu.cvc.uab.imagedetect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionsActivity extends Activity {

	private static final String TAG = "Detector::QuestionsActivity";
	static {
		if (!OpenCVLoader.initDebug()) {
			// TODO Handle initialization error
		} else {
			Log.i(TAG, "OpenCV loaded successfully");
			/* read text_extractor */
			System.loadLibrary("image_extractor");
		}
	}

	protected static final String THRESHOLD = "THRESHOLD";
	protected static final String NUMMINFEATS = "NUMINFEATS";
	protected static final String TARGETS = "TARGETS";
	protected static final String THRESHOLDVOTES = "THRESHOLDVOTES";
	protected static final String PATHMODEL = "PATHMODEL";
	protected static final String CURRENTTARGET = "CURRENTTARGET";
	public static final String POINTS = "POINTS";
	public static final String INCREMENT_POINTS = "INCREMENT_POINTS";
	public static final String NUM_MAX_QUEST = "NUM_MAX_QUEST";
	public static final String NUM_MAX_TRIES = "NUM_MAX_TRIES";

	public static final int DETECTOR = 0;
	public static final int RESULT_VIEW = 1;

	public static final int QUIT = 2;

	private Map<String, Pair<Integer, List<String>>> questions;

	private double threshold = 1000;
	private double thresholdVotes = 1000;
	private int numMinFeatures;
	private String pathModel;
	private String[] targets;
	private String currentTarget;
	private QuestionLoader questionLoader;

	private int countPoints;
	private int numQuestions;
	private int numTries;

	private static int SIZE_POINTS = 10;
	private static int NUMMAXQUESTIONS = 5;
	private static int MAX_NUM_TRIES = 4;

	public static Properties Properties;

	public QuestionsActivity() {
		this.questions = new HashMap<String, Pair<Integer, List<String>>>();
	}

	private void setPoints(int numberPoints) {
		TextView view = (TextView) this.findViewById(R.id.points);
		view.setText(String.valueOf(numberPoints));
	}

	private void resetPointsValues() {
		this.numQuestions = 0;
		this.countPoints = 0;
		this.numTries = 0;
	}

	private void startResultActivity() {
		Intent intent = new Intent(getBaseContext(), ResultActivity.class);
		intent.putExtra(POINTS, countPoints);
		intent.putExtra(INCREMENT_POINTS, SIZE_POINTS);
		intent.putExtra(NUM_MAX_QUEST, NUMMAXQUESTIONS);
		intent.putExtra(NUM_MAX_TRIES, MAX_NUM_TRIES);
		resetPointsValues();
		startActivityForResult(intent, RESULT_VIEW);
	}

	@Override
	public void onResume() {
		super.onResume();
		setPoints(countPoints);

		// FIXME IMPORTAT ONLY FOR TESTING DELETE THIS LINE
		// /startShowActivity(0);
	}

	// FIXME DELETE THIS METHOD, BECAUSE HERE IS NOT NECESSARY

	/*
	 * private void startShowActivity(int idAuthor) { Intent intent = new
	 * Intent(this, ShowActivity.class);
	 * intent.putExtra(DetectorActivity.IDAUTHOR, idAuthor);
	 * startActivity(intent); }
	 */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.button_question_views);

		try {
			try {
				Properties = new Properties(this);
				threshold = Properties.getThreshold();
				thresholdVotes = Properties.getThresholdVotes();
				numMinFeatures = Properties.getNumMinFeatures();
				pathModel = Properties.getPathModel();
				targets = Properties.getTargets();
			} catch (IOException e) {
				// TODO notifications
				this.notification(getString(R.string.cannot_read));
				this.notification("It was not possible read properties configuration");
				throw e;
			}

			loadQuestions();
			resetPointsValues();
			// Init random current target
			this.currentTarget = randomTarget();
			Pair<Integer, List<String>> result = questions.get(currentTarget);
			updateHint(result.second.get(0));
			this.questions.put(currentTarget, new Pair<Integer, List<String>>(1, result.second));

			Button enableCamera = (Button) findViewById(R.id.button_enable_camera);
			enableCamera.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(getBaseContext(), DetectorActivity.class);
					newIntent.putExtra(THRESHOLD, threshold);
					newIntent.putExtra(NUMMINFEATS, numMinFeatures);
					newIntent.putExtra(THRESHOLDVOTES, thresholdVotes);
					newIntent.putExtra(TARGETS, targets);
					newIntent.putExtra(PATHMODEL, pathModel);
					newIntent.putExtra(CURRENTTARGET, currentTarget);
					startActivityForResult(newIntent, DETECTOR);

				}
			});

			Button newHint = (Button) findViewById(R.id.button_new_hint);
			newHint.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Pair<Integer, List<String>> result = questions.get(currentTarget);
					int index = result.first;
					if (index == result.second.size()) {
						// reset number of questions
						questions.put(currentTarget, new Pair<Integer, List<String>>(0, result.second));
						currentTarget = randomTarget(currentTarget);
						index = 0;
						result = questions.get(currentTarget);
						numQuestions += 1;
						numTries = 0;

						if (numQuestions != NUMMAXQUESTIONS) {
							new_author_notification(getString(R.string.new_author));
						}
						if (numQuestions == NUMMAXQUESTIONS) {
							// notification(getString(R.string.new_play));
							startResultActivity();
							resetPointsValues();
						}

					}
					updateHint(result.second.get(index));
					index = index + 1;
					numTries += 1;
					questions.put(currentTarget, new Pair<Integer, List<String>>(index, result.second));

				}

			});

		} catch (IOException e) {
			Utils.PrintException(e);
		}

	}

	private void updateHint(String string) {
		TextView hint = (TextView) this.findViewById(R.id.hint_text_view);
		hint.setText(string);
	}

	private String randomTarget(String oldValue) {
		String newValue;
		do {
			int possibleTargets = this.targets.length;
			int intRandomValue = new Random(Calendar.getInstance().getTimeInMillis()).nextInt(possibleTargets);
			newValue = this.targets[intRandomValue];
		} while (newValue.equalsIgnoreCase(oldValue));

		return newValue;
	}

	private String randomTarget() {
		int possibleTargets = this.targets.length;
		int intRandomValue = new Random(Calendar.getInstance().getTimeInMillis()).nextInt(possibleTargets);
		return this.targets[intRandomValue];
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == DETECTOR) {

			if (resultCode == Activity.RESULT_OK) {
				// Init random current target
				// Choose a new author
				currentTarget = randomTarget(currentTarget);
				// Load questions for new author
				Pair<Integer, List<String>> result = questions.get(currentTarget);
				// Show new question
				updateHint(result.second.get(0));
				// Update tries
				questions.put(currentTarget, new Pair<Integer, List<String>>(1, result.second));
				numQuestions += 1;
				countPoints += (MAX_NUM_TRIES - numTries) * SIZE_POINTS;
				numTries = 0;

				setPoints(countPoints);
				// I did max questions
				if (numQuestions == NUMMAXQUESTIONS) {
					// notification(getString(R.string.finished_game));
					startResultActivity();
					resetPointsValues();
				}

			} else if (resultCode == Activity.RESULT_CANCELED) {

				Pair<Integer, List<String>> result = questions.get(currentTarget);
				int index = result.first;
				if (index == result.second.size()) {
					// reset number of questions
					questions.put(currentTarget, new Pair<Integer, List<String>>(0, result.second));
					currentTarget = randomTarget(currentTarget);
					index = 0;
					result = questions.get(currentTarget);
					numQuestions += 1;
					numTries = 0;

					if (numQuestions != NUMMAXQUESTIONS) {
						new_author_notification(getString(R.string.new_author));
					}
					if (numQuestions == NUMMAXQUESTIONS) {
						// notification(getString(R.string.new_play));
						startResultActivity();
						resetPointsValues();
					}

				}

				updateHint(result.second.get(index));
				index = index + 1;
				numTries += 1;
				questions.put(currentTarget, new Pair<Integer, List<String>>(index, result.second));

			}

		} else if (requestCode == RESULT_VIEW) {
			if (resultCode == this.QUIT) {
				finish();
			}
		}
	}

	private void loadQuestions() {
		this.questionLoader = new QuestionLoader(this);
		for (String target : targets) {
			List<String> listOfQuestions = new ArrayList<String>();
			this.questionLoader.load(target, listOfQuestions);
			this.questions.put(target, new Pair(0, listOfQuestions));
		}

	}

	public void notification(final String value) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(QuestionsActivity.this, value, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	public void new_author_notification(final String value) {

		runOnUiThread(new Runnable() {
			public void run() {

				View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastLayout));

				TextView textView = (TextView) toastView.findViewById(R.id.text);
				textView.setText(value);

				Toast goodtoast = new Toast(QuestionsActivity.this);
				goodtoast.setDuration(Toast.LENGTH_LONG);
				goodtoast.setView(toastView);

				goodtoast.show();
			}
		});
	}

}
