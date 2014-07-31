package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class helpMessage extends Activity {
	private static String m_message;

	private static boolean isStringMessage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpmessage);

		WebView web = new WebView(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout_main);
		layout.addView(web);

		if (true == isStringMessage) {
			web.loadData(PrinterTypeActivity.HTMLCSS() + m_message, "text/html", "utf-16");
		} else {
			web.loadUrl(m_message);
		}
	}

	public static void SetMessage(String message) {
		m_message = message;
		isStringMessage = true;
	}

	public static void SetHTML(String url) {
		m_message = url;
		isStringMessage = false;
	}
}
