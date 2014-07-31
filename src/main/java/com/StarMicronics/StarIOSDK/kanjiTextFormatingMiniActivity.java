package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.StarMicronics.StarIOSDK.PrinterFunctions.Alignment;

public class kanjiTextFormatingMiniActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mini_kanjitextformating);

		Spinner spinner_height = (Spinner) findViewById(R.id.spinner_height);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});
		spinner_height.setAdapter(ad);

		Spinner spinner_width = (Spinner) findViewById(R.id.spinner_width);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});
		spinner_width.setAdapter(ad);

		Spinner spinner_alignment = (Spinner) findViewById(R.id.spinner_alignment);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Left", "Center", "Right"});
		spinner_alignment.setAdapter(ad);
	}

	public void PrintText(View view) {
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		CheckBox checkbox_underline = (CheckBox) findViewById(R.id.checkbox_underline);
		boolean underline = checkbox_underline.isChecked();

		CheckBox checkbox_emphasized = (CheckBox) findViewById(R.id.checkbox_emphasized);
		boolean emphasized = checkbox_emphasized.isChecked();

		CheckBox checkbox_upsidedown = (CheckBox) findViewById(R.id.chechbox_upsidedown);
		boolean upsidedown = checkbox_upsidedown.isChecked();

		CheckBox checkbox_invertColor = (CheckBox) findViewById(R.id.checkbox_invertcolor);
		boolean invertColor = checkbox_invertColor.isChecked();

		Spinner spinner_height = (Spinner) findViewById(R.id.spinner_height);
		byte height = (byte) spinner_height.getSelectedItemPosition();

		Spinner spinner_width = (Spinner) findViewById(R.id.spinner_width);
		byte width = (byte) spinner_width.getSelectedItemPosition();

		EditText edittext_leftmargin = (EditText) findViewById(R.id.edittext_leftmargin);
		int leftMargin = 0;
		try {
			leftMargin = Integer.parseInt(edittext_leftmargin.getText().toString());
		} catch (Exception e) {
			leftMargin = 0;
		}

		Spinner spinner_alignment = (Spinner) findViewById(R.id.spinner_alignment);
		PrinterFunctions.Alignment alignment = PrinterFunctions.Alignment.Left;
		switch (spinner_alignment.getSelectedItemPosition()) {
			case 0:
				alignment = Alignment.Left;
				break;
			case 1:
				alignment = Alignment.Center;
				break;
			case 2:
				alignment = Alignment.Right;
		}

		EditText edittext_texttoprint = (EditText) findViewById(R.id.editText_TextToPrint);
		byte[] texttoprint = edittext_texttoprint.getText().toString().getBytes();

		MiniPrinterFunctions.PrintTextKanji(this, portName, portSettings, underline, emphasized, upsidedown, invertColor, height, width, leftMargin, alignment, texttoprint);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<UnderlineTitle>Specify / Cancel Shift JIS Kanji character mode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>FS C <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1C 43 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"<rightMov>n = 1 or 0 (on or off)</rightMov><br/><br/>\n" +
				"<UnderlineTitle>Underline Command</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC - <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 2D <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>n</rightMov> <rightMov_NOI>= 0,1, or 2</rightMov_NOI><br/>" +
				"<rightMov>0</rightMov> <rightMov_NOI>= Turns off underline mode</rightMov_NOI><br/>" +
				"<rightMov>1</rightMov> <rightMov_NOI>= Turns on underline mode (1 dot thick)</rightMov_NOI><br/><br/>" +
				"<rightMov>2</rightMov> <rightMov_NOI>= Turns on underline mode (2 dots thick)</rightMov_NOI><br/><br/><br/>" +
				"<UnderlineTitle>Emphasized Mode</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC E <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 45 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>n</rightMov> <rightMov_NOI>= 1 or 0 (on or off)</rightMov_NOI><br/><br/>" +
				"<UnderlineTitle>Upside Down</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC { <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 7B <StandardItalic>n</StandardItalic></CodeDef></br/><br/>" +
				"<rightMov>n</rightMov><rightMov_NOI>= 1 or 0 (on or off)</rightMov_NOI><br/><br/>" +
				"<UnderlineTitle>Invert Color</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>GS B <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				" <Code>Hex:</Code> <CodeDef>1D 42 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>n</rightMov> <rightMov_NOI>= 1 or 0 (on or off)</rightMov_NOI><br/><br/>" +
				"<UnderlineTitle>Set character size</UnderlineTitle><br/><br/>" +
				"<Code>ASCII:</Code> <CodeDef>GS ! <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1D 21 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>1&#8804;height multiple times normal font size&#8804;8</rightMov><br/><br/>" +
				"<rightMov>1&#8804;width  multiple times normal font size&#8804;8</rightMov><br/><br/>" +
				"<rightMov>n represents both height and width expansions.  Bit 0 to 2 sets the character width. Bit 4 to 6 sets the character height</rightMov><br/><br/<br/><br/><br/>" +
				"<UnderlineTitle>Left Margin</UnderlineTitle><br/><br/>" +
				"<Code>ACSII:</Code> <CodeDef>GS L <StandardItalic>nL nH</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1D 4C <StandardItalic>nL nH</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>nL</rightMov> <rightMov_NOI>Lower order number for left margin.  Mathematically: margin &#37; 256</rightMov_NOI><br/><br/><br/>" +
				"<rightMov>nH</rightMov> <rightMov_NOI>Higher order number for left margin.  Mathematically: margin / 256</rightMov_NOI><br/><br/><br/><br/>" +
				"<UnderlineTitle>Alignment</UnderlineTitle><br/><br/>" +
				"<Code>ACSII:</Code> <CodeDef>ESC a <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 61 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>n = 48 (left) or 49 (center) or 50 (right)</rightMov>" +
				"</body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
