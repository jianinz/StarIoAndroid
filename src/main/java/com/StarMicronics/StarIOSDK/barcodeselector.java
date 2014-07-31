package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class barcodeselector extends Activity implements OnItemClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcodeselector);

		ListView listViewBarCode = (ListView) findViewById(R.id.listView_barcodeList);
		ListAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Code39", "Code93", "ITF", "Code128"});

		listViewBarCode.setAdapter(ad);
		listViewBarCode.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) {
			Intent myIntent = new Intent(this, code39Activity.class);
			startActivityFromChild(this, myIntent, 0);
		} else if (arg2 == 1) {
			Intent myIntent = new Intent(this, code93Activity.class);
			startActivityFromChild(this, myIntent, 0);
		} else if (arg3 == 2) {
			Intent myIntent = new Intent(this, ITFActivity.class);
			startActivityFromChild(this, myIntent, 0);
		} else if (arg3 == 3) {
			Intent myIntent = new Intent(this, code128Activity.class);
			startActivityFromChild(this, myIntent, 0);
		}
	}
}
