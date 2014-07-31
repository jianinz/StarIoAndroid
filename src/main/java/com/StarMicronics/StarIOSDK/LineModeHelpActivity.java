package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LineModeHelpActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		TextView textView_help = (TextView) findViewById(R.id.textView_Help);
		String helpText = getResources().getString(R.string.lineModeHelpMessage);
		textView_help.setText(helpText);
	}
}