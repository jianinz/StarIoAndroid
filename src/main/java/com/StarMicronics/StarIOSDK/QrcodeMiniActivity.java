package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.StarMicronics.StarIOSDK.PrinterFunctions.CorrectionLevelOption;

public class QrcodeMiniActivity extends Activity implements OnItemSelectedListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mini_qrcode);

		Spinner spinner_CorrectionLevel = (Spinner) findViewById(R.id.spinner_CorrectionLevel);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"L 7%", "M 15%", "Q 25%", "H 30%"});
		spinner_CorrectionLevel.setAdapter(ad);
		spinner_CorrectionLevel.setOnItemSelectedListener(this);

		Spinner spinner_sizeByEC = (Spinner) findViewById(R.id.spinner_qrcode_size);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Auto Size",
				"19", "34", "55", "80",
				"108", "136", "156", "194",
				"232", "274", "324", "370",
				"428", "461", "523", "589",
				"647", "721", "795", "861",
				"932", "1006", "1094", "1174",
				"1276", "1370", "1468", "1531",
				"1631", "1735", "1843", "1955",
				"2071", "2191", "2306", "2434",
				"2566", "2702", "2812", "2956"});
		spinner_sizeByEC.setAdapter(ad);

		Spinner spinner_ModelSize = (Spinner) findViewById(R.id.spinner_module_size);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4",
				"5", "5", "6", "7", "8"});
		spinner_ModelSize.setAdapter(ad);
		spinner_ModelSize.setSelection(4);
	}

	public void PrintBarCode(View view) {
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		PrinterFunctions.CorrectionLevelOption correctionLevel = CorrectionLevelOption.Low;
		Spinner spinner_CorrectionLevel = (Spinner) findViewById(R.id.spinner_CorrectionLevel);
		switch (spinner_CorrectionLevel.getSelectedItemPosition()) {
			case 0:
				correctionLevel = CorrectionLevelOption.Low;
				break;
			case 1:
				correctionLevel = CorrectionLevelOption.Middle;
				break;
			case 2:
				correctionLevel = CorrectionLevelOption.Q;
				break;
			case 3:
				correctionLevel = CorrectionLevelOption.High;
				break;
		}

		Spinner spinner_SizebyEC = (Spinner) findViewById(R.id.spinner_qrcode_size);
		byte sizeByEC = (byte) spinner_SizebyEC.getSelectedItemPosition();

		Spinner spinner_ModuleSize = (Spinner) findViewById(R.id.spinner_module_size);
		byte moduleSize = (byte) (spinner_ModuleSize.getSelectedItemPosition() + 1);

		EditText editText_barcodedata = (EditText) findViewById(R.id.editText_qrcode_data);
		byte[] barcodedata = editText_barcodedata.getText().toString().getBytes();

		MiniPrinterFunctions.PrintQrcode(this, portName, portSettings, correctionLevel, sizeByEC, moduleSize, barcodedata);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Spinner spinner_sizeByEC = (Spinner) findViewById(R.id.spinner_qrcode_size);
		if (arg2 == 0) {
			SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Auto Size",
					"19", "34", "55", "80",
					"108", "136", "156", "194",
					"232", "274", "324", "370",
					"428", "461", "523", "589",
					"647", "721", "795", "861",
					"932", "1006", "1094", "1174",
					"1276", "1370", "1468", "1531",
					"1631", "1735", "1843", "1955",
					"2071", "2191", "2306", "2434",
					"2566", "2702", "2812", "2956"});
			spinner_sizeByEC.setAdapter(ad);
		} else if (arg2 == 1) {
			SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Auto Size",
					"16", "28", "44", "64",
					"86", "108", "124", "154",
					"182", "216", "254", "290",
					"334", "365", "415", "453",
					"507", "563", "627", "669",
					"714", "782", "860", "914",
					"1000", "1062", "1128", "1193",
					"1267", "1373", "1455", "1541",
					"1631", "1725", "1812", "1914",
					"1992", "2102", "2216", "2334"});
			spinner_sizeByEC.setAdapter(ad);
		} else if (arg2 == 2) {
			SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Auto Size",
					"13", "22", "34", "48",
					"62", "76", "88", "110",
					"132", "154", "180", "206",
					"244", "261", "295", "325",
					"367", "397", "445", "485",
					"512", "568", "614", "664",
					"718", "754", "808", "871",
					"911", "985", "1033", "1115",
					"1171", "1231", "1286", "1354",
					"1426", "1502", "1582", "1666"});
			spinner_sizeByEC.setAdapter(ad);
		} else if (arg2 == 3) {
			SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Auto Size",
					"9", "16", "26", "36",
					"46", "60", "66", "86",
					"100", "122", "140", "158",
					"180", "197", "223", "253",
					"283", "313", "341", "385",
					"406", "442", "464", "514",
					"538", "596", "628", "661",
					"701", "745", "793", "845",
					"901", "961", "986", "1054",
					"1096", "1142", "1222", "1276"});
			spinner_sizeByEC.setAdapter(ad);
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		//
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<UnderlineTitle>Set Qrcode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>GS Z STX</CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1D 5A 02</CodeDef><br/><br/>" +
				"* Note: This code must be used before the other QRcode command.  It sets the printer to QRCode<br/><br/>" +
				"<UnderlineTitle>Print QRCode</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC Z <StandardItalic>m n k dL dH d1 ... dk</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 90 <StandardItalic>m n k dL dH d1 ... dk</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>m</rightMov> <rightMov_NOI>specifies verion symbol (1&#8804;m&#8804;40 or 0 is autosize) see manual (pg 41) for more information</rightMov_NOI><br/><br/><br/>" +
				"<rightMov>n</rightMov> <rightMov_NOI>specifies EC Level.  This value can be 4C, 4D, 51, or 48 in Hex.  It stands for L, M, Q, H respectively</rightMov_NOI><br/><br/><br/>" +
				"<rightMov>k</rightMov> <rightMov_NOI>specifies module size (1&#8804;k&#8804;8)</rightMov_NOI><br/>" +
				"<rightMov>dL</rightMov> <rightMov_NOI>Represents the lower byte describing the number of bytes in the qrcode.  Mathematically: dL = qrcode Length &#37; 256</rightMov_NOI><br/><br/><br/><br/>" +
				"<rightMov>dH</rightMov> <rightMov_NOI>Represents the higher byte describing the number of bytes in the qrcode.  Mathematically: dH = qrcode Length / 256</rightMov_NOI><br/><br/><br/><br/>" +
				"<rightMov>d1 ... dk</rightMov> <rightMov_NOI2>This is the text that will be placed in the qrcode.</rightMov_NOI2>" +
				"</body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
