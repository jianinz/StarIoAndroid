package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.StarMicronics.StarIOSDK.PrinterFunctions.CorrectionLevelOption;
import com.StarMicronics.StarIOSDK.PrinterFunctions.Model;

public class QRCodeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode);

		Spinner spinner_CorrectionLevel = (Spinner) findViewById(R.id.spinner_CorrectionLevel);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"L 7%", "M 15%", "Q 25%", "H 30%"});
		spinner_CorrectionLevel.setAdapter(ad);

		Spinner spinner_Model = (Spinner) findViewById(R.id.spinner_Model);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Model 1", "Model 2"});
		spinner_Model.setAdapter(ad);

		Spinner spinner_CellSize = (Spinner) findViewById(R.id.spinner_CellSize);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"});
		spinner_CellSize.setAdapter(ad);
		spinner_CellSize.setSelection(4);
	}

	public void PrintBarCode(View view) {
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		Spinner spinner_CorrectionLevel = (Spinner) findViewById(R.id.spinner_CorrectionLevel);
		PrinterFunctions.CorrectionLevelOption correctionLevel = CorrectionLevelOption.Low;
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

		Spinner spinner_Model = (Spinner) findViewById(R.id.spinner_Model);
		PrinterFunctions.Model model = Model.Model1;

		switch (spinner_Model.getSelectedItemPosition()) {
			case 0:
				model = Model.Model1;
				break;
			case 1:
				model = Model.Model2;
				break;
		}

		Spinner spinner_CellSize = (Spinner) findViewById(R.id.spinner_CellSize);
		byte cellSize = (byte) (spinner_CellSize.getSelectedItemPosition() + 1);
		EditText textView = (EditText) findViewById(R.id.editText_QRCodeData);
		byte[] barCodeData = textView.getText().toString().getBytes();

		PrinterFunctions.PrintQrCode(this, portName, portSettings, correctionLevel, model, cellSize, barCodeData);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<body><p>StarMicronics supports all the latest high data density" +
				"for QR codes.  QR Codes  are handy for distributing URLs, Music, Images, E-Mails, " +
				"Contacts and much more.  Is is public domain and great for storing Japanese Kanji " +
				"and Kana characters.  They can be scanned with almost all smart phones which is great " +
				"if you want to for example, put a QR Code to hyperlink you company's Facebook profile on " +
				"the bottom of every receipt. <br/><br/>" +
				"<SectionHeader>(1) Set barcode model</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS y S 0 <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 79 53 30 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(2)Set error correction level</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS y S 1 <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDeF>1B 1D 79 53 31 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(3)Specify size of cell</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS y S 2 <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 79 53 32 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(4)Set barcode data</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS y D 1 NUL <StandardItalic>nL nH d1d2 ... dk</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 79 44 31 00 <StandardItalic>nL nH d1d2 ... dk</StandardItalic></CodeDef><br/><br/>" +
				"<SectionHeader>(5)Print barcode</SectionHeader><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS y P</CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 79 50</CodeDef><br/></br>" +
				"* Note the QR code is a registered trademark of DENSO WEB" +
				"</body><html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
