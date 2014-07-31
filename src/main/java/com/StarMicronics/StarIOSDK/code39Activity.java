package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.StarMicronics.StarIOSDK.PrinterFunctions.BarCodeOption;
import com.StarMicronics.StarIOSDK.PrinterFunctions.NarrowWide;

public class code39Activity extends Activity {

	int selectedOption = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode);

		Spinner Spinner_Narrow_Wide = (Spinner) findViewById(R.id.spinner_Narrow_Wide);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"2:6", "3:9", "4:12", "2:5", "3:8", "4:10", "2:4", "3:6", "4:8"});

		Spinner_Narrow_Wide.setAdapter(ad);

		Spinner Spinner_Layout = (Spinner) findViewById(R.id.spinner_layout_code39);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"No under-bar character & execute line feed", "Add under-bar characters & execute line feed", "No under-bar characters & do not execute line feed", "Add under-bar characters & do not execute line feed"});
		Spinner_Layout.setAdapter(ad);

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
		if (height > 255) {
			height = 255;
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
		PrinterFunctions.NarrowWide width = NarrowWide._2_4;
		switch (widthIndex) {
			case 0:
				width = NarrowWide._2_6;
				break;
			case 1:
				width = NarrowWide._3_9;
				break;
			case 2:
				width = NarrowWide._4_12;
				break;
			case 3:
				width = NarrowWide._2_5;
				break;
			case 4:
				width = NarrowWide._3_8;
				break;
			case 5:
				width = NarrowWide._4_10;
				break;
			case 6:
				width = NarrowWide._2_4;
				break;
			case 7:
				width = NarrowWide._3_6;
				break;
			case 8:
				width = NarrowWide._4_8;
				break;
		}

		PrinterFunctions.PrintCode39(this, portName, portSettings, data, option, (byte) height, width);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<body>" +
				"<Code>ASCII:</Code>" +
				"<CodeDef>ESC b EOT <StandardItalic>n2 n3 n4 d1 ... dk</StandardItalic> RS</CodeDef></br>" +
				"<Code>Hex:</Code> <CodeDef>1B 62 04 <StandardItalic>n2 n3 n4 d1 ... nk</StandardItalic> 1E</CodeDef><br/><br/>" +
				"<TitleBold>Code 39</TitleBold> represents numbers 0 to 9 and the letters of the alphabet from A to Z.  " +
				"These are the symbols most frequently used today in industry." +
				"</body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
