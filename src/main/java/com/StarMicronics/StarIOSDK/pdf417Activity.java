package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.StarMicronics.StarIOSDK.PrinterFunctions.Limit;

public class pdf417Activity extends Activity implements OnItemSelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pft417code);

		Spinner spinner_barcodeSize = (Spinner) findViewById(R.id.spinner_BarcodeSize);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Use Limits", "Use Fixed"});
		spinner_barcodeSize.setAdapter(ad);
		spinner_barcodeSize.setOnItemSelectedListener(this);

		Display display = getWindowManager().getDefaultDisplay();

		EditText editText_Height = (EditText) findViewById(R.id.editText_Height);
		editText_Height.setWidth(display.getWidth() / 2);

		EditText editText_Width = (EditText) findViewById(R.id.editText_Width);
		editText_Width.setWidth(display.getWidth() / 2);

		TextView textView_Height = (TextView) findViewById(R.id.textView_Height);
		textView_Height.setWidth(display.getWidth() / 2);

		Spinner spinner_AspectRatio = (Spinner) findViewById(R.id.spinner_AspectRatio);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
		spinner_AspectRatio.setAdapter(ad);

		Spinner spinner_XDirection = (Spinner) findViewById(R.id.spinner_XDirection);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
		spinner_XDirection.setAdapter(ad);

		Spinner spinner_SecurityLevel = (Spinner) findViewById(R.id.spinner_SecurityLevel1);
		SpinnerAdapter ad1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"});
		spinner_SecurityLevel.setAdapter(ad1);

	}

	public void PrintBarCode(View view) {
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		PrinterFunctions.Limit limit = Limit.USE_LIMITS;
		Spinner spinner_barcodeSize = (Spinner) findViewById(R.id.spinner_BarcodeSize);
		switch (spinner_barcodeSize.getSelectedItemPosition()) {
			case 0:
				limit = Limit.USE_LIMITS;
				break;
			case 1:
				limit = Limit.USE_FIXED;
				break;
		}

		EditText editText_Height = (EditText) findViewById(R.id.editText_Height);
		byte height = 1;
		try {
			height = (byte) Integer.parseInt(editText_Height.getText().toString());
		} catch (Exception e) {
			height = 1;
		}

		EditText editText_Width = (EditText) findViewById(R.id.editText_Width);
		byte width = 1;
		try {
			width = (byte) Integer.parseInt(editText_Width.getText().toString());
		} catch (Exception e) {
			width = 1;
		}

		if (limit == Limit.USE_LIMITS) {
			if ((height > 99) || (height == 0))
				height = 1;
			if ((width > 99) || (width == 0))
				width = 99;
		} else {
			if ((height > 0) && (height < 3)) {
				height = 3;
			}
			if (height > 90) {
				height = 3;
			}
			if (width > 30) {
				width = 1;
			}
		}

		Spinner spinner_aspectRatio = (Spinner) findViewById(R.id.spinner_AspectRatio);
		byte aspectRatio = (byte) (spinner_aspectRatio.getSelectedItemPosition() + 1);

		Spinner spinner_xDirection = (Spinner) findViewById(R.id.spinner_XDirection);
		byte xDirection = (byte) (spinner_xDirection.getSelectedItemPosition() + 1);

		Spinner spinner_securityLevel = (Spinner) findViewById(R.id.spinner_SecurityLevel1);
		byte securityLevel = (byte) spinner_securityLevel.getSelectedItemPosition();

		EditText editText_barcodeData = (EditText) findViewById(R.id.editText_pdf417_barcodeData);
		byte[] barcodeData = editText_barcodeData.getEditableText().toString().getBytes();
		PrinterFunctions.PrintPDF417Code(this, portName, portSettings, limit, height, width, securityLevel, xDirection, aspectRatio, barcodeData);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		TextView textView_Height = (TextView) findViewById(R.id.textView_Height);
		TextView textView_Width = (TextView) findViewById(R.id.textView_Width);
		if (arg2 == 0) {
			textView_Height.setText("height=1≤h≤99");
			textView_Width.setText("width=1≤w≤99");
		} else {
			textView_Height.setText("height=0,3≤h≤90");
			textView_Width.setText("width=0,1≤w≤30");
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		//
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<body>" +
				"<p>PDF417 Barcodes are public domain.  They can contain a much " +
				"larger amount of data because it can link one barcode to another " +
				"to create one portable data file from many PDF417 barcodes.</p> " +
				"<SectionHeader>(1)Size setting<SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x S 0 <StandardItalic>n p1 p2</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 53 30 <StandardItalic>n p1 p2</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(2)ECC (Security Level)</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x S 1 <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 53 31 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(3)Module x direction size</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x S 2 <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 53 32 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(4)Module aspect ratio</SectionHeader><br>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x S 3 <StandardItalic>n</StandardItalic></CodeDef><br>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 53 33 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(5)Data setting</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x D <StandardItalic>nL nH d1d2 ... dk</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 44 <StandardItalic>nL nH d1d2 ... dk</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(6)Printing</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x P</CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 50</CodeDef><br/><br/>" +
				"<SectionHeader>(7)Expansion Info</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS x 1</CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 78 49</CodeDef><br/<br/>" +
				"For more information on PDF417 commands, please read 2d Barcode " +
				"PDF417 section in the Thermal Line Mode Command Specification Manual.";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
