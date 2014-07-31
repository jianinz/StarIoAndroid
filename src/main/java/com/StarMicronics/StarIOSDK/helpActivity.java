package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class helpActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		TextView textView_help = (TextView) findViewById(R.id.textView_Help);
		String helpText = "PORT NAME PARAMETERS\nIf using WLAN/Ethernet...\nTCP:192.168.222.244\nEnter your IP address\n\nIf using USB...\nUSB: (No value)\n\nIf using Bluetooth...\nBT: (No value) Uses first Star printer paired with the device\nBT:Device_Name\nBT:MAC Address\n\nPORT SETTINGS PARAMERERS\nPort Settings should be blank for Star POS printers\n\nPort Settings should be 'mini' for Star portable printers\n\n\n\nVersion Number 3.8";
		textView_help.setText(helpText);
	}
}
