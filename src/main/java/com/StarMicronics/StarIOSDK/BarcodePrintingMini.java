package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.StarMicronics.StarIOSDK.MiniPrinterFunctions.BarcodeType;
import com.StarMicronics.StarIOSDK.MiniPrinterFunctions.BarcodeWidth;

public class BarcodePrintingMini extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mini_barcode);

		Spinner spinner_Width = (Spinner) findViewById(R.id.spinner_Width);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"0.125", "0.25", "0.375", "0.5", "0.625", "0.75", "0.875", "1.0"});
		spinner_Width.setAdapter(ad);

		Spinner spinner_Barcode = (Spinner) findViewById(R.id.spinner_barcodeType);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Code39", "ITF", "Code93", "Code128"});
		spinner_Barcode.setAdapter(ad);
	}

	public void PrintBarCodeData(View view) {
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		EditText editText = (EditText) findViewById(R.id.editText_Height);
		String sheight = editText.getText().toString();
		if (sheight.equals("")) {
			sheight = "60";
		}
		int height = Integer.parseInt(sheight);
		if (height > 255) {
			height = 255;
		}

		MiniPrinterFunctions.BarcodeWidth width = BarcodeWidth._125;
		Spinner spinner_Width = (Spinner) findViewById(R.id.spinner_Width);
		switch (spinner_Width.getSelectedItemPosition()) {
			case 0:
				width = BarcodeWidth._125;
				break;
			case 1:
				width = BarcodeWidth._250;
				break;
			case 2:
				width = BarcodeWidth._375;
				break;
			case 3:
				width = BarcodeWidth._500;
				break;
			case 4:
				width = BarcodeWidth._625;
				break;
			case 5:
				width = BarcodeWidth._750;
				break;
			case 6:
				width = BarcodeWidth._875;
				break;
			case 7:
				width = BarcodeWidth._1_0;
				break;
		}

		MiniPrinterFunctions.BarcodeType type = BarcodeType.code39;
		Spinner spinner_Barcode = (Spinner) findViewById(R.id.spinner_barcodeType);
		switch (spinner_Barcode.getSelectedItemPosition()) {
			case 0:
				type = BarcodeType.code39;
				break;
			case 1:
				type = BarcodeType.ITF;
				break;
			case 2:
				type = BarcodeType.code93;
				break;
			case 3:
				type = BarcodeType.code128;
				break;
		}

		EditText editText_barcodeData = (EditText) findViewById(R.id.editText_barcodeData);
		String data = editText_barcodeData.getText().toString();
		byte[] barcodeData = data.getBytes();
		MiniPrinterFunctions.PrintBarcode(this, portName, portSettings, (byte) height, width, type, barcodeData);
	}

	public void Help(View view) {
		String helpString = "<UnderlineTitle>Set Barcode Height</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>GS h <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1D 68 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"* Note: <StandardItalic>n</StandardItalic> must be between 0 and 255<br/><br/>" +
				"<UnderlineTitle>Set barcode width</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>GS w <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1D 77 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov> 1&#60;n&#60;8</rightMov><br/><br/>" +
				"<table border=\"1\" BORDERCOLOR=\"black\" cellspacing=0 bgcolor=\"#FFFFCC\">" +
				"<tr>" +
				"<td rowspan=\"2\" width=\"30\" bgcolor=\"#800000\"><font color=\"white\"><center>n</center></font></td>" +
				"<td rowspan=\"2\" width=\"125\" bgcolor=\"#800000\"><font color=\"white\"><center>Multi-Level Barcode Module width(mm)</center></font></td>" +
				"<td rowspan=\"1\" colspan=\"2\" bgcolor=\"#800000\"><font color=\"white\"><center>Binary Level Barcode</center></font></td>" +
				"</tr>" +
				"<tr>" +
				"<td bgcolor=\"#800000\"><font color=\"white\"><center>Thin Element width(mm)</center></font></td>" +
				"<td bgcolor=\"#800000\"><font color=\"white\"><center>Thick Element width(mm)</center></font></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>1</center></td>" +
				"<td><center>0.125</center></td>" +
				"<td><center>0.125</center></td>" +
				"<td><center>0.375 (= 0.125 * 3 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>2</center></td>" +
				"<td><center>0.25</center></td>" +
				"<td><center>0.25</center></td>" +
				"<td><center>0.625 ( = 0.25 * 2.5 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>3</center></td>" +
				"<td><center>0.375</center></td>" +
				"<td><center>0.375</center></td>" +
				"<td><center>1.125 ( = 0.375 * 3 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>4</center></td>" +
				"<td><center>0.5</center></td>" +
				"<td><center>0.5</center></td>" +
				"<td><center>1.25 ( = 0.5 * 2.5 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>5</center></td>" +
				"<td><center>0.625</center></td>" +
				"<td><center>0.625</center></td>" +
				"<td><center>1.875 ( = 0.625 * 3 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>6</center></td>" +
				"<td><center>0.75</center></td>" +
				"<td><center>0.75</center></td>" +
				"<td><center>1.875 ( = 0.75 * 2.5 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>7</center></td>" +
				"<td><center>0.875</center></td>" +
				"<td><center>0.875</center></td>" +
				"<td><center>2.625 ( = 0.875 * 3 )</center></td>" +
				"</tr>" +
				"<tr>" +
				"<td><center>8</center></td>" +
				"<td><center>1.0</center></td>" +
				"<td><center>1.0</center></td>" +
				"<td><center>2.5 ( = 1.0 * 2.5 )</center></td>" +
				"</tr>" +
				"</table><br/>" +
				"<UnderlineTitle>Print barcode</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>GS k <StandardItalic>m d1 ... dk</StandardItalic> NUL</CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1D 6B <StandardItalic>m d1 ... dk</StandardItalic> 00</CodeDef><br/><br/>" +
				"<rightMov>m = Barcode Type See manual (pg 35)</rightMov><br/>" +
				"<rightMov>d1 ... dk = Barcode data. see manual (pg 35) for supported characters</rightMov></body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
