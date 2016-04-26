package edu.cvc.uab.imagedetect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
	private static final String TAG = "Detector::ResultActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.result_view);
		Intent intent = getIntent();
		int points = intent.getIntExtra(QuestionsActivity.POINTS, -1);
		if (points == -1) {
			notification(getString(R.string.new_play));
			finish();
		}

		int points_size = intent.getIntExtra(QuestionsActivity.INCREMENT_POINTS, -1);
		int num_max_quest = intent.getIntExtra(QuestionsActivity.NUM_MAX_QUEST, -1);
		int num_max_tries = intent.getIntExtra(QuestionsActivity.NUM_MAX_TRIES, -1);

		int num_points = (points_size * num_max_tries * num_max_quest) / 4;
		String result_text;

		TextView resultView = (TextView) findViewById(R.id.result_comment);

		if (points < num_points) {
			result_text = getString(R.string.result_bad);
			resultView.setTextColor(Color.rgb(200, 0, 0));
		} else if (points < (num_points * 2)) {
			result_text = getString(R.string.result_regular);
			resultView.setTextColor(Color.rgb(255, 128, 0));
		} else if (points < (num_points * 3)) {
			result_text = getString(R.string.result_good);
			resultView.setTextColor(Color.rgb(0, 200, 0));
		} else {
			result_text = getString(R.string.result_excelent);
			resultView.setTextColor(Color.rgb(0, 0, 200));
		}

		resultView.setText(result_text);

		TextView pointsView = (TextView) findViewById(R.id.points);
		pointsView.setText(String.valueOf(points));

		Button restartButton = (Button) findViewById(R.id.restart_game);
		restartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button quitButton = (Button) findViewById(R.id.quit_game);
		quitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(QuestionsActivity.QUIT);
				finish();
			}
		});

	}

	public void notification(final String value) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(ResultActivity.this, value, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
}
