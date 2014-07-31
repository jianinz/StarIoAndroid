package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import java.util.ArrayList;
import java.util.List;

public class StarIOSDKMobilePrinterActivity extends Activity {

	private Context me = this;
	private String strPrintArea = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.main);

		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		SharedPreferences pref = getSharedPreferences("pref", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
		portNameField.setText(pref.getString("m_portName", "TCP:192.168.192.45"));

		InitializeComponent();
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void Test(View view) {
		if (!checkClick.isClickEvent()) return;
		Intent myIntent = new Intent(this, helpActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;
		Intent myIntent = new Intent(this, helpActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void OpenCashDrawer(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		String portName = portNameField.getText().toString();
		String portSettings = "mini";

		MiniPrinterFunctions.OpenCashDrawer(this, portName, portSettings);

	}

	public void GetStatus(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		String portName = portNameField.getText().toString();
		String portSettings = "mini";

		//The portable printer and non portable printer have the same
		MiniPrinterFunctions.CheckStatus(this, portName, portSettings);
	}

	public void ShowBarcode(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		Intent myIntent = new Intent(this, BarcodePrintingMini.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowBarcode2d(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		Intent myIntent = new Intent(this, barcodeselector2d.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowCut(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());

		PrinterTypeActivity.setPortSettings("mini");
		MiniPrinterFunctions.performCut(this);
	}

	public void ShowTextFormating(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		Intent myIntent = new Intent(this, textFormatingMiniActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowKanjiTextFormating(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		Intent myIntent = new Intent(this, kanjiTextFormatingMiniActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowRasterPrinting(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		Intent myIntent = new Intent(this, rasterPrintingActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowImagePrinting(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		Intent myIntent = new Intent(this, imagePrintingActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void MCR(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings("mini");

		MiniPrinterFunctions.MCRStart(this, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings());
	}

	public void SampleReceipt(View view) {
		if (!checkClick.isClickEvent()) return;
		//showDialog(DIALOG_PRINTABLEAREA_ID);
		final String item_list[] = new String[]{
				getResources().getString(R.string.printArea2inch),
				getResources().getString(R.string.printArea3inch),
				getResources().getString(R.string.printArea4inch),
		};

		strPrintArea = getResources().getString(R.string.printArea2inch);

		Builder printableAreaDialog = new AlertDialog.Builder(this);
		printableAreaDialog.setIcon(android.R.drawable.checkbox_on_background);
		printableAreaDialog.setTitle("Printable Area List");
		printableAreaDialog.setCancelable(false);
		printableAreaDialog.setSingleChoiceItems(item_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				strPrintArea = item_list[whichButton];
			}
		});
		printableAreaDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
				((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);

				EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
				PrinterTypeActivity.setPortName(portNameField.getText().toString());
				PrinterTypeActivity.setPortSettings("mini");

				MiniPrinterFunctions.PrintSampleReceipt(me, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings(), strPrintArea);
			}
		});
		printableAreaDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		printableAreaDialog.show();
	}

	public void SampleRecieptJp(View view) {
		if (!checkClick.isClickEvent()) return;
		final String item_list[] = new String[]{
				getResources().getString(R.string.printArea2inch),
				getResources().getString(R.string.printArea3inch),
				getResources().getString(R.string.printArea4inch),
		};

		strPrintArea = getResources().getString(R.string.printArea2inch);

		Builder printableAreaDialog = new AlertDialog.Builder(this);
		printableAreaDialog.setIcon(android.R.drawable.checkbox_on_background);
		printableAreaDialog.setTitle("Printable Area List");
		printableAreaDialog.setCancelable(false);
		printableAreaDialog.setSingleChoiceItems(item_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				strPrintArea = item_list[whichButton];
			}
		});
		printableAreaDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
				((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);

				EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
				PrinterTypeActivity.setPortName(portNameField.getText().toString());
				PrinterTypeActivity.setPortSettings("mini");

				MiniPrinterFunctions.PrintSampleReceiptJp(me, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings(), strPrintArea);
			}
		});
		printableAreaDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		printableAreaDialog.show();
	}

	public void PortDiscovery(View view) {
		if (!checkClick.isClickEvent()) return;
		List<PortInfo> BTPortList;

		final EditText editPortName;
		final ArrayList<PortInfo> arrayDiscovery;

		ArrayList<String> arrayPortName;

		arrayDiscovery = new ArrayList<PortInfo>();
		arrayPortName = new ArrayList<String>();

		try {
			BTPortList = StarIOPort.searchPrinter("BT:");    // "port discovery" of Portable printer is support only Bluetooth port

			for (PortInfo portInfo : BTPortList) {
				arrayDiscovery.add(portInfo);
			}

			arrayPortName = new ArrayList<String>();

			for (PortInfo discovery : arrayDiscovery) {
				String portName;

				portName = discovery.getPortName();

				if (discovery.getMacAddress().equals("") == false) {
					portName += "\n - " + discovery.getMacAddress();
					if (discovery.getModelName().equals("") == false) {
						portName += "\n - " + discovery.getModelName();
					}
				}
				arrayPortName.add(portName);
			}

		} catch (StarIOPortException e) {
			e.printStackTrace();
		}

		editPortName = new EditText(this);

		new AlertDialog.Builder(this)
				.setIcon(R.drawable.icon)
				.setTitle("Please Select IP Address or Input Port Name")
				.setCancelable(false)
				.setView(editPortName)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int button) {
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);
						EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
						portNameField.setText(editPortName.getText());

						SharedPreferences pref = getSharedPreferences("pref", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
						Editor editor = pref.edit();
						editor.putString("m_portName", portNameField.getText().toString());
						editor.commit();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int button) {
					}
				})
				.setItems(arrayPortName.toArray(new String[0]), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int select) {
						EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
						portNameField.setText(arrayDiscovery.get(select).getPortName());

						SharedPreferences pref = getSharedPreferences("pref", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
						Editor editor = pref.edit();
						editor.putString("m_portName", portNameField.getText().toString());
						editor.commit();
					}
				})
				.show();
	}

	private void InitializeComponent() {    // delete view of some function button for Portable Printer
		TextView titleText = (TextView) findViewById(R.id.textView1);
		titleText.setText("Star Micronics Portable Printer Samples");

		Button cashDrawerButton = (Button) findViewById(R.id.button_OpenCashDrawer);
		cashDrawerButton.setVisibility(View.GONE);

		Button cutButton = (Button) findViewById(R.id.button_Cut);
		cutButton.setVisibility(View.GONE);

		TextView portNumberText = (TextView) findViewById(R.id.textView2);
		portNumberText.setVisibility(View.GONE);

		TextView bluetoothText = (TextView) findViewById(R.id.textView3);
		bluetoothText.setVisibility(View.GONE);

		TextView sensorActiveText = (TextView) findViewById(R.id.textView4);
		sensorActiveText.setVisibility(View.GONE);

		Spinner spinner_tcp_port_number = (Spinner) findViewById(R.id.spinner_tcp_port_number);
		spinner_tcp_port_number.setVisibility(View.GONE);

		Spinner spinner_bluetooth_communication_type = (Spinner) findViewById(R.id.spinner_bluetooth_communication_type);
		spinner_bluetooth_communication_type.setVisibility(View.GONE);

		Spinner spinner_sensor_active = (Spinner) findViewById(R.id.spinner_SensorActive);
		spinner_sensor_active.setVisibility(View.GONE);

	}
}