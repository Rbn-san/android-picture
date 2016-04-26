package edu.cvc.uab.imagedetect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.author_view);

		correct_answer_notification(getString(R.string.correct_author));

		int idAuthor = this.getIntent().getIntExtra(DetectorActivity.IDAUTHOR, -1);

		ImageLoad();

		String id_author = Integer.toString(idAuthor);
		String filename = id_author + ".txt";

		StringBuilder text = new StringBuilder();
		StringBuilder name = new StringBuilder();

		InputStream is;
		try {
			is = this.getResources().getAssets().open(filename);
			// is = this.getResources().openRawResource(R.raw.filename);

			BufferedReader br = new BufferedReader(new InputStreamReader(is, "8859_1"));
			/*
			 * String line;
			 * 
			 * while ((line = br.readLine()) != null) { text.append(line);
			 * text.append('\n'); } br.close();
			 */

			String next, line = br.readLine();
			for (boolean first = true, last = (line == null); !last; first = false, line = next) {
				last = ((next = br.readLine()) == null);

				if (first) {
					name.append(line);

				} else {
					text.append(line).append('\n');
				}
			}
			br.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Utils.PrintException(e1);
		}

		TextView author_name = (TextView) findViewById(R.id.author_name);
		author_name.setText(name.toString());

		TextView info = (TextView) findViewById(R.id.show_info);
		info.setText(text.toString());

		// /String htmlText = " %s ";

		// /WebView info = (WebView) findViewById(R.id.show_info);
		// /info.loadData(String.format(htmlText, text), "text/html", "utf-8");

		String link = QuestionsActivity.Properties.getLinkTarget(idAuthor);
		TextView author_link = (TextView) findViewById(R.id.show_link);
		author_link.setText(link);

		Button continue_game = (Button) findViewById(R.id.continue_game);
		continue_game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				// startActivity(new Intent(ShowActivity.this,
				// QuestionsActivity.class));

			}
		});

	}

	public void ImageLoad() {

		Mat m = new Mat();
		// FIXME change this variable for a correct image
		m = Highgui.imread("/sdcard/temp.png", Highgui.CV_LOAD_IMAGE_COLOR);
		Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2RGB);
		// convert to bitmap:
		Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
		// Utils.matToBitmap(m, bm);
		org.opencv.android.Utils.matToBitmap(m, bm);
		// find the image view and draw it!
		ImageView iv = (ImageView) findViewById(R.id.author_capture);
		iv.setImageBitmap(bm);
	}

	public void correct_answer_notification(final String value) {

		View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastLayout));

		ImageView bien = (ImageView) toastView.findViewById(R.id.image);
		bien.setImageResource(R.drawable.correct);

		TextView textView = (TextView) toastView.findViewById(R.id.text);
		textView.setText(value);

		Toast goodtoast = new Toast(ShowActivity.this);
		goodtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		goodtoast.setDuration(Toast.LENGTH_SHORT);
		goodtoast.setView(toastView);

		goodtoast.show();

	}
}
