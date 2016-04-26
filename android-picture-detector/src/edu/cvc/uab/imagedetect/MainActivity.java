package edu.cvc.uab.imagedetect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Get the view from menu_start.xml
		setContentView(R.layout.menu_start);

		// Locate the button in activity_main.xml
		Button start_game = (Button) findViewById(R.id.button_start);

		// Capture button clicks
		start_game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start NewActivity.class
				Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
				startActivity(intent);
			}
		});
	}

}
