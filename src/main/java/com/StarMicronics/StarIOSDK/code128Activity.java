package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.StarMicronics.StarIOSDK.PrinterFunctions.BarCodeOption;
import com.StarMicronics.StarIOSDK.PrinterFunctions.Min_Mod_Size;

public class code128Activity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode);

		Spinner Spinner_Narrow_Wide = (Spinner) findViewById(R.id.spinner_Narrow_Wide);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"2 dots", "3 dots", "4 dots"});

		Spinner_Narrow_Wide.setAdapter(ad);

		Spinner Spinner_Layout = (Spinner) findViewById(R.id.spinner_layout_code39);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"No under-bar character & execute line feed", "Add under-bar characters & execute line feed", "No under-bar characters & do not execute line feed", "Add under-bar characters & do not execute line feed"});
		Spinner_Layout.setAdapter(ad);

		ImageView codeImage = (ImageView) findViewById(R.id.imageView1);
		codeImage.setImageResource(R.drawable.code128);

		Spinner spinner_font = (Spinner) findViewById(R.id.spinner_Font_barcode1d);
		TextView textView_font = (TextView) findViewById(R.id.textView_Font);
		spinner_font.setVisibility(View.GONE);
		textView_font.setVisibility(View.GONE);
	}

	public void PrintBarCode(View view) {
		if (!checkClick.isClickEvent()) return;

		Spinner Spinner_Narrow_Wide = (Spinner) findViewById(R.id.spinner_Narrow_Wide);
		Spinner Spinner_Layout = (Spinner) findViewById(R.id.spinner_layout_code39);
		EditText barCode = (EditText) findViewById(R.id.editText_BarcodeData);
		String text = barCode.getText().toString();
		byte[] data = text.getBytes();

		EditText textEditHeight = (EditText) findViewById(R.id.editText_Height);
		int height = 80;
		try {
			height = Integer.parseInt(textEditHeight.getText().toString());
		} catch (Exception e) {
			height = 80;
		}

		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		int optionIndex = Spinner_Layout.getSelectedItemPosition();
		PrinterFunctions.BarCodeOption option = BarCodeOption.Adds_Characters_With_Line_Feed;
		switch (optionIndex) {
			case 0:
				option = BarCodeOption.No_Added_Characters_With_Line_Feed;
				break;
			case 1:
				option = BarCodeOption.Adds_Characters_With_Line_Feed;
				break;
			case 2:
				option = BarCodeOption.No_Added_Characters_Without_Line_Feed;
				break;
			case 3:
				option = BarCodeOption.Adds_Characters_Without_Line_Feed;
				break;
		}

		int widthIndex = Spinner_Narrow_Wide.getSelectedItemPosition();
		PrinterFunctions.Min_Mod_Size size = Min_Mod_Size._2_dots;
		switch (widthIndex) {
			case 0:
				size = Min_Mod_Size._2_dots;
				break;
			case 1:
				size = Min_Mod_Size._3_dots;
				break;
			case 2:
				size = Min_Mod_Size._4_dots;
				break;
		}

		PrinterFunctions.PrintCode128(this, portName, portSettings, data, option, (byte) height, size);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;

		String helpString = "<body>" +
				"<Code>Ascii:</Code> <CodeDef>ESC b ACK <StandardItalic>n2 n3 n4 d1 ... dk</StandardItalic> RS</CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 62 04 <StandardItalic>n2 n3 n4 d1 ... dk</StandardItalic> 1E</CodeDef><br/><br/>" +
				"<TitleBold>Code 128</TitleBold> can print ASCII 128 characters.  For that reason, " +
				"use thereof is increasing." +
				"</body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
