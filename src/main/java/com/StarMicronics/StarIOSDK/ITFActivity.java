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
import com.StarMicronics.StarIOSDK.PrinterFunctions.NarrowWideV2;

public class ITFActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode);

		Spinner Spinner_Narrow_Wide = (Spinner) findViewById(R.id.spinner_Narrow_Wide);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"2:5", "4:10", "6:15", "2:4", "4:8", "6:12", "2:6", "3:9", "4:12"});

		Spinner_Narrow_Wide.setAdapter(ad);

		Spinner Spinner_Layout = (Spinner) findViewById(R.id.spinner_layout_code39);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"No under-bar character & execute line feed", "Add under-bar characters & execute line feed", "No under-bar characters & do not execute line feed", "Add under-bar characters & do not execute line feed"});
		Spinner_Layout.setAdapter(ad);

		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setImageResource(R.drawable.itf);

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
		PrinterFunctions.NarrowWideV2 width = NarrowWideV2._2_5;
		switch (widthIndex) {
			case 0:
				width = NarrowWideV2._2_5;
				break;
			case 1:
				width = NarrowWideV2._4_10;
				break;
			case 2:
				width = NarrowWideV2._6_15;
				break;
			case 3:
				width = NarrowWideV2._2_4;
				break;
			case 4:
				width = NarrowWideV2._4_8;
				break;
			case 5:
				width = NarrowWideV2._6_12;
				break;
			case 6:
				width = NarrowWideV2._2_6;
				break;
			case 7:
				width = NarrowWideV2._3_9;
				break;
			case 8:
				width = NarrowWideV2._4_12;
				break;
		}

		PrinterFunctions.PrintCodeITF(this, portName, portSettings, data, option, (byte) height, width);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<body>" +
				"<Code>Ascii:</Code> <CodeDef>ESC b ENQ <StandardItalic>n2 n3 n4 d1 ... dk</StandardItalic> RS</CodeDef> " +
				"<Code>Hex:</Code> <CodeDef>1B 62 04 <StandardItalic>n2 n3 n4 d1 ... dk</StandardItalic> 1E</CodeDef><br/><br/>" +
				"<TitleBold>Interleaved 2 of 5</TitleBold> represents numbers 0 to 9 and the total digits must be even.  " +
				"Each pattern can hold 2 digits, one in the black bars and one in the white spaces.  " +
				"Higher density of characters is possible and with JIS and EAN, and printing to cardboard for distribution " +
				"has been standardized." +
				"</body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
