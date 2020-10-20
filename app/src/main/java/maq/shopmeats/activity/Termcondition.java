package maq.shopmeats.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import maq.shopmeats.R;

import static maq.shopmeats.activity.MainActivity.changeStatsBarColor;
import static maq.shopmeats.activity.MainActivity.tf_opensense_regular;


public class Termcondition extends Activity {

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.activity_termcondition);
		changeStatsBarColor(Termcondition.this);
		ImageButton ib_back = findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		setTermsCondition();
	}
	private void setTermsCondition() {
		((TextView)findViewById(R.id.txt_name)).setTypeface(tf_opensense_regular);

		WebView web = findViewById(R.id.web);
		web.loadUrl("file:///android_asset/"+getString(R.string.terms_condition_filename));
	}


}
