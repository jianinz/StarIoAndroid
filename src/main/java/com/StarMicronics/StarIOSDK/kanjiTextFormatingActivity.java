package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.StarMicronics.StarIOSDK.PrinterFunctions.Alignment;

public class kanjiTextFormatingActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kanjitextformating);

		Spinner spinner_Height = (Spinner) findViewById(R.id.spinner_Height);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6"});
		spinner_Height.setAdapter(ad);

		Spinner spinner_Width = (Spinner) findViewById(R.id.spinner_Width);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6"});
		spinner_Width.setAdapter(ad);

		Spinner spinner_Alignment = (Spinner) findViewById(R.id.spinner_Alignment);
		ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Left", "Center", "Right"});
		spinner_Alignment.setAdapter(ad);
	}

	public void PrintText(View view) {
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		RadioButton radio_ShiftJIS = (RadioButton) findViewById(R.id.radio_ShiftJIS);
		boolean shiftJIS = false;
		if (radio_ShiftJIS.isChecked() == true) {
			shiftJIS = true;
		}

		CheckBox checkbox_underline = (CheckBox) findViewById(R.id.checkBox_Underline);
		boolean underline = checkbox_underline.isChecked();

		CheckBox checkbox_invertcolor = (CheckBox) findViewById(R.id.checkBox_InvertColor);
		boolean invertColor = checkbox_invertcolor.isChecked();

		CheckBox checkbox_emphasized = (CheckBox) findViewById(R.id.checkBox_Emphasized);
		boolean emphasized = checkbox_emphasized.isChecked();

		CheckBox checkbox_upperline = (CheckBox) findViewById(R.id.checkBox_Upperline);
		boolean upperline = checkbox_upperline.isChecked();

		CheckBox checkbox_upsideDown = (CheckBox) findViewById(R.id.checkBox_UpsideDown);
		boolean upsideDown = checkbox_upsideDown.isChecked();

		Spinner spinner_Height = (Spinner) findViewById(R.id.spinner_Height);
		int heightExpansion = spinner_Height.getSelectedItemPosition();

		Spinner spinner_Width = (Spinner) findViewById(R.id.spinner_Width);
		int widthExpansion = spinner_Width.getSelectedItemPosition();

		EditText editText_margin = (EditText) findViewById(R.id.editText_LeftMargin);
		byte leftMargin = 0;
		try {
			leftMargin = (byte) Integer.parseInt(editText_margin.getText().toString());
		} catch (Exception e) {
			leftMargin = 0;
		}

		Spinner spinner_Alignment = (Spinner) findViewById(R.id.spinner_Alignment);
		PrinterFunctions.Alignment alignment = Alignment.Left;
		switch (spinner_Alignment.getSelectedItemPosition()) {
			case 0:
				alignment = Alignment.Left;
				break;
			case 1:
				alignment = Alignment.Center;
				break;
			case 2:
				alignment = Alignment.Right;
				break;
		}

		EditText editText_TextToPrint = (EditText) findViewById(R.id.editText_TextToPrint);
		byte[] textToPrint = editText_TextToPrint.getText().toString().getBytes();
		PrinterFunctions.PrintTextKanji(this, portName, portSettings, shiftJIS, underline, invertColor, emphasized, upperline, upsideDown, heightExpansion, widthExpansion, leftMargin, alignment, textToPrint);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "<UnderlineTitle>Specify JIS Kanji character mode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC p </CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 70 </CodeDef><br/><br/>\n" +
				"More info in section 3.3.15 of the Line Mode Manual<br/><br/>\n" +
				"<UnderlineTitle>Cancel JIS Kanji character mode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC q </CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 71 </CodeDef><br/><br/>\n" +
				"More info in section 3.3.15 of the Line Mode Manual<br/><br/>\n" +
				"<UnderlineTitle>Specify / Cancel Shift JIS Kanji character mode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC $ <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 24 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"<rightMov>n = 1 or 0 (on or off)</rightMov><br/><br/>\n" +
				"More info in section 3.3.15 of the Line Mode Manual<br/><br/>\n" +
				"<UnderlineTitle>Character Expansion Settings</UnderlineTitle><br/><br/>\n" +
				"Width Expansion<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC W <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 57 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"Height Expansion<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC h <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 68 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"<rightMov>n = 0 through 5 (Multiplier)</rightMov><br/><br/>\n" +
				"More info in section 3.3.2. of the Line Mode Manual.<br/><br/>\n" +
				"<UnderlineTitle>Emphasized Printing (Bold)</UnderlineTitle><br/><br/>\n" +
				"Start Bold Text<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC E\n</CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 45\n</CodeDef><br/><br/>\n" +
				"Stop Bold Text<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC F</CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 46</CodeDef><br/><br/>\n" +
				"More info in section 3.3.3 of the Line Mode Manual<br/><br/>\n" +
				"<UnderlineTitle>Underline Mode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC - <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 2D <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"<rightMov>n = 1 or 0 (on or off)</rightMov><br/><br/>\n" +
				"<UnderlineTitle>Upperline Mode</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC _ <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 5F <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"<rightMov>n = 1 or 0 (on or off)</rightMov><br/><br/>\n" +
				"<UnderlineTitle>White/Black Inverted Color Mode</UnderlineTitle><br/><br/>\n" +
				"Start B/W Invert<br/>" +
				"<Code>ASCII:</Code> <CodeDef>ESC 4</CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 34</CodeDef><br/><br/>\n" +
				"Stop B/W Invert<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC 5</CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1b 35</CodeDef><br/><br/>\n" +
				"<UnderlineTitle>Upside-Down Printing</UnderlineTitle><br/><br/>\n" +
				"Start B/W Invert<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>SI</CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>0F</CodeDef><br/><br/>\n" +
				"Stop B/W Invert<br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>DC2</CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>12</CodeDef><br/><br/>\n" +
				"Note: When using the command, only use it at the start of a new line. Rightside-up and Upside-Down text cannot be on the same line.<br/><br/>" +
				"<UnderlineTitle>Set Left Margin</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC l <StandardItalic>n</StandardItalic></CodeDef><br/>\n" +
				"<Code>Hex:</Code> <CodeDef>1B 6C <StandardItalic>n</StandardItalic></CodeDef><br/><br/>\n" +
				"<rightMov>n = 0 through 255</rightMov><br/><br/>\n" +
				"More info in section 3.3.6 of the Line Model Manual.<br/><br/>\n" +
				"<UnderlineTitle>Set Text Alignment</UnderlineTitle><br/><br/>\n" +
				"<Code>ASCII:</Code> <CodeDef>ESC GS a <StandardItalic>n</StandardItalic></CodeDef><br/>" +
				"<Code>Hex:</Code> <CodeDef>1B 1D 61 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
				"<rightMov>n = 0 (left) or 1 (center) or 2 (right)</rightMov>" +
				"</body></html>";
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}
}
