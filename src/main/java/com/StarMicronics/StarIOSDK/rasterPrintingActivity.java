package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class rasterPrintingActivity extends Activity implements OnItemSelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printingtextasimage);

		Spinner spinner_Font = (Spinner) findViewById(R.id.spinner_Font);
		SpinnerAdapter ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Default", "MonoSpace", "Serif", "Sans"});

		spinner_Font.setAdapter(ad);
		spinner_Font.setOnItemSelectedListener(this);

		CheckBox italics = (CheckBox) findViewById(R.id.checkbox_italics);
		italics.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SetTextBox();

			}
		});

		CheckBox checkbox_bold = (CheckBox) findViewById(R.id.checkbox_bold);
		checkbox_bold.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SetTextBox();

			}
		});

		CheckBox checkbox_CompressAPI = (CheckBox) findViewById(R.id.checkbox_CompressAPI);
		CheckBox checkbox_pageMode = (CheckBox) findViewById(R.id.checkbox_pageMode);

		checkbox_CompressAPI.setChecked(true);

		Spinner spinner_paperWidth = (Spinner) findViewById(R.id.spinner_paperwidth);

		SpinnerAdapter ad_paperWidth;

		if (PrinterTypeActivity.getPortSettings().toUpperCase().equals("MINI")) {
			ad_paperWidth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"2inch", "3inch", "4inch"});
		} else {
			checkbox_pageMode.setVisibility(View.GONE);
			ad_paperWidth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"3inch", "4inch"});
		}

		spinner_paperWidth.setAdapter(ad_paperWidth);

		EditText edittext_TextToPrint = (EditText) findViewById(R.id.editText_TextToPrint);
		float textSize = edittext_TextToPrint.getTextSize();

		EditText edittext_textsize = (EditText) findViewById(R.id.editText_TextSize);
		edittext_textsize.setText(Float.toString(textSize));

		EditText editText = (EditText) findViewById(R.id.editText_TextSize);
		editText.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				SetTextBox();
				return false;
			}
		});

		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		SetTextBox();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		//
	}

	public void SetTextBox() {
		Spinner spinner_Font = (Spinner) findViewById(R.id.spinner_Font);

		EditText text = (EditText) findViewById(R.id.editText_TextToPrint);
		Typeface typeface = Typeface.MONOSPACE;
		switch (spinner_Font.getSelectedItemPosition()) {
			case 0:
				typeface = Typeface.DEFAULT;
				break;
			case 1:
				typeface = Typeface.MONOSPACE;
				break;
			case 2:
				typeface = Typeface.SERIF;
				break;
			case 3:
				typeface = Typeface.SANS_SERIF;
				break;
		}

		CheckBox checkBox_italics = (CheckBox) findViewById(R.id.checkbox_italics);
		int style = 0;
		if (checkBox_italics.isChecked() == true) {
			style = style | Typeface.ITALIC;
		}

		CheckBox checkBox_bold = (CheckBox) findViewById(R.id.checkbox_bold);
		if (checkBox_bold.isChecked() == true) {
			style = style | Typeface.BOLD;
		}

		EditText editText = (EditText) findViewById(R.id.editText_TextSize);
		float textSize = 27;
		try {
			textSize = Float.parseFloat(editText.getText().toString());
		} catch (Exception e) {
			textSize = 27;
		}
		;
		text.setTypeface(typeface, style);
		text.setTextSize(textSize);
	}

	public void PrintText(View view) {
		if (!checkClick.isClickEvent()) return;
		CheckBox checkBox_italics = (CheckBox) findViewById(R.id.checkbox_italics);
		int style = 0;
		boolean italics = false;
		boolean compressionEnable = false;
		boolean pageModeEnable = false;

		if (checkBox_italics.isChecked() == true) {
			italics = true;
		}

		CheckBox checkBox_bold = (CheckBox) findViewById(R.id.checkbox_bold);
		if (checkBox_bold.isChecked() == true) {
			style = style | Typeface.BOLD;
		}

		CheckBox checkbox_CompressAPI = (CheckBox) findViewById(R.id.checkbox_CompressAPI);
		if (checkbox_CompressAPI.isChecked() == true) {
			compressionEnable = true;
		}

		CheckBox checkbox_pageMode = (CheckBox) findViewById(R.id.checkbox_pageMode);
		if (checkbox_pageMode.isChecked() == true) {
			pageModeEnable = true;
		}

		Spinner spinner_Font = (Spinner) findViewById(R.id.spinner_Font);
		Typeface font = Typeface.DEFAULT;
		switch (spinner_Font.getSelectedItemPosition()) {
			case 0:
				font = Typeface.DEFAULT;
				break;
			case 1:
				font = Typeface.MONOSPACE;
				break;
			case 2:
				font = Typeface.SERIF;
				break;
			case 3:
				font = Typeface.SANS_SERIF;
				break;
		}

		//paint.setTypeface(typeface)

		EditText editText_textsize = (EditText) findViewById(R.id.editText_TextSize);
		float textSize = 27;
		try {
			textSize = Float.parseFloat(editText_textsize.getText().toString());
		} catch (Exception e) {
			textSize = 27;
		}
		;

		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();

		Spinner spinner_paperWidth = (Spinner) findViewById(R.id.spinner_paperwidth);
		int paperWidth = 0;

		if (portSettings.toUpperCase().equals("MINI")) {
			paperWidth = 408;
			switch (spinner_paperWidth.getSelectedItemPosition()) {
				case 0:
					paperWidth = 408;    // 2inch (408 dot)
					break;
				case 1:
					paperWidth = 576;    // 3inch (576 dot)
					break;
				case 2:
					paperWidth = 832;    // 4inch (832 dot)
					break;
			}
		} else {
			paperWidth = 576;
			switch (spinner_paperWidth.getSelectedItemPosition()) {
				case 0:
					paperWidth = 576;    // 3inch (576 dot)
					break;
				case 1:
					paperWidth = 832;    // 4inch (832 dot)
					break;
			}
		}

		EditText editText_text = (EditText) findViewById(R.id.editText_TextToPrint);
		String textToPrint = editText_text.getText().toString();

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		Typeface typeface = Typeface.create(font, style);
		paint.setTypeface(typeface);
		paint.setTextSize(textSize * 2);
		TextPaint textpaint = new TextPaint(paint);
		if (italics) {
			textpaint.setTextSkewX((float) -0.25);
		}

		android.text.StaticLayout staticLayout = new StaticLayout(textToPrint, textpaint, paperWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
		int height = staticLayout.getHeight();

		if ((portSettings.toUpperCase().equals("MINI")) && (true == pageModeEnable)) {
			if (height > 2378) {
				height = 2378;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(staticLayout.getWidth(), height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bitmap);
		c.drawColor(Color.WHITE);
		c.translate(0, 0);
		staticLayout.draw(c);

		if (portSettings.toUpperCase().equals("MINI")) {
			MiniPrinterFunctions.PrintBitmap(this, portName, portSettings, bitmap, paperWidth, compressionEnable, pageModeEnable);
		} else {
			PrinterFunctions.PrintBitmap(this, portName, portSettings, bitmap, paperWidth, compressionEnable);
		}
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		String helpString = "";
		String portSettings = PrinterTypeActivity.getPortSettings();
		if (portSettings.toUpperCase().equals("MINI")) {
			helpString = "<UnderlineTitle>Define Bit Image</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC X 4 <StandardItalic>x y d1...dk</StandardItalic></CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 58 34 <StandardItalic>x y d1...dk</StandardItalic></CodeDef><br/><br/>" +
					"<rightMov>x</rightMov> <rightMov_NOI>Width of the image divided by 8</rightMov_NOI><br/>" +
					"<rightMov>y</rightMov> <rightMov_NOI>Vertical number of dots to be printed.  This value shouldn't exceed 24.  If you need to print an image taller than 24 then you should use this command multiple times</rightMov_NOI><br/><br/><br/><br/><br/><br/>" +
					"<rightMov>d1...dk</rightMov> <rightMov_NOI2>The dots that should be printed.  When all vertical dots are printed the head moves horizonaly to the next vertical set of dots</rightMov_NOI2><br/><br/><br/><br/><br/><br/>" +
					"<UnderlineTitle>Print Bit Image</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC X 2 <StandardItalic>y</StandardItalic></CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 58 32<StandardItalics>y</StandardItalics></CodeDef><br/><br/>" +
					"<rightMov>y</rightMov> <rightMov_NOI>The value y must be the same value that was used int eh ESC X 4 command for define a bit image</rightMov_NOI><br/><br/><br/><br/>" +
					"Note: The command ESC X 2 must be used after each usage of ESC X 4 inorder to print images";
		} else {
			helpString = "<UnderlineTitle>Enter Raster Mode</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r A</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 41</CodeDef><br/><br/>" +
					"<UnderlineTitle>Initiallize raster mode</UnderlineTitle><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r R</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 52</CodeDef><br/><br/>" +
					"<UnderlineTitle>Set Raster EOT mode</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r E <StandardItalic>n</StandardItalic> NUL</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 45 <StandardItalic>n</StandardItalic> 00</CodeDef><br/>" +
					"<div class=\"div-tableCut\">" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>n</center></div>" +
					"<div class=\"div-table-colRaster\"><center>FormFeed</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Cut Feed</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Cutter</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Presenter</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>0</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>1</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>2</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>3</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>TearBar</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>8</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>9</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>12</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Partial Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>13</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Partial Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>36</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Eject</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>37</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Eject</center></div>" +
					"</div>" +
					"</div><br/><br/>" +
					"<UnderlineTitle>Set Raster FF mode</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r F <StandardItalic>n</StandardItalic> NUL</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 46 <StandardItalic>n</StandardItalic> 00</CodeDef><br/>" +
					"<div class=\"div-tableCut\">" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>n</center></div>" +
					"<div class=\"div-table-colRaster\"><center>FormFeed</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Cut Feed</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Cutter</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Presenter</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>0</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Default</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>1</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>2</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>3</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>TearBar</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>8</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>9</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>12</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Partial Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>13</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Partial Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>36</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>-</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Eject</center></div>" +
					"</div>" +
					"<div class=\"div-table-rowCut\">" +
					"<div class=\"div-table-colRaster\"><center>37</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>&#x25CB;</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Full Cut</center></div>" +
					"<div class=\"div-table-colRaster\"><center>Eject</center></div>" +
					"</div>" +
					"</div><br/><br/>" +
					"<UnderlineTitle>Set raster page length</UnderlineTitle><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r P <StandardItalic>n</StandardItalic> NUL</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 50 <StandardItalic>n</StandardItalic> NUL</CodeDef><br/><br/>" +
					"<rightMov>0 = Continuous print mode (no page length setting)</rightMov><br/><br/>" +
					"<rightMov>1&#60;n = Specify page length</rightMov><br/><br/>" +
					"<UnderlineTitle>Set raster print quality</UnderlineTitle><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r Q <StandardItalic>n</StandardItalic> NUL</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 51 <StandardItalic>n</StandardItalic> 00</CodeDef><br/><br/>" +
					"<rightMov>0 = Specify high speed printing</rightMov><br/>" +
					"<rightMov>1 = Normal print quality</rightMov><br/>" +
					"<rightMov>2 = High print quality</rightMov><br/><br/>" +
					"<UnderlineTitle>Set raster left margin</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r m l <StandardItalic>n</StandardItalic> NUL</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 6D 6C <StandardItalic>n</StandardItalic> 00</CodeDef><br/><br/>" +
					"<UnderlineTitle>Send raster data (auto line feed)</UnderlineTitle><br/><br/>" +
					"<Code>ASCII:</Code> <CodeDef>b <StandardItalic>n1 n2 d1 d2 ... dk</StandardItalic></CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>62 <StandardItalic>n1 n2 d1 d2 ... dk</StandardItalic></CodeDef><br/><br/>" +
					"<rightMov>n1 = (image width / 8) Mod 256</rightMov><br/>" +
					"<rightMov>n2 = image width / 256</rightMov><br/>" +
					"<rightMov>k = n1 + n2 * 256</rightMov><br/>" +
					"* Each byte send in d1 ... dk represents 8 horizontal bits.  The values n1 and n2 tell the printer how many byte are sent with d1 ... dk.  The printer automatically feeds when the last value for d1 ... dk is sent<br/><br/>" +
					"<UnderlineTitle>Quit raster mode</UnderlineTitle><br/></br>" +
					"<Code>ASCII:</Code> <CodeDef>ESC * r A</CodeDef><br/>" +
					"<Code>Hex:</Code> <CodeDef>1B 2A 72 41</CodeDef><br/><br/>" +
					"* This command automatically executes a EOT(cut) command before quiting.  Use the set EOT command to change the action of this command.";
		}
		helpMessage.SetMessage(helpString);

		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	}

}
