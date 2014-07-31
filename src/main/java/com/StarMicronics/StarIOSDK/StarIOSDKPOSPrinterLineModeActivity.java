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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import java.util.ArrayList;
import java.util.List;

public class StarIOSDKPOSPrinterLineModeActivity extends Activity {

	private Context me = this;
	private String strPrintArea = "";
	private String strInterface = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.main);

		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		portNameField.setText(pref.getString("portName", "TCP:192.168.192.45"));

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
		String portSettings = getPortSettingsOption(portName);

		PrinterFunctions.OpenCashDrawer(this, portName, portSettings);
	}

	public void GetStatus(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		String portName = portNameField.getText().toString();
		String portSettings = getPortSettingsOption(portName);

		//The portable printer and non portable printer have the same
		PrinterFunctions.CheckStatus(this, portName, portSettings, getSensorActiveHigh());
	}

	public void ShowBarcode(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, barcodeselector.class);
		startActivityFromChild(this, myIntent, 0);

	}

	public void ShowBarcode2d(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, barcodeselector2d.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowCut(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, cutActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowTextFormating(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, textFormatingActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowKanjiTextFormating(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, kanjiTextFormatingActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowRasterPrinting(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, rasterPrintingActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void ShowImagePrinting(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());

		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		Intent myIntent = new Intent(this, imagePrintingActivity.class);
		startActivityFromChild(this, myIntent, 0);
	}

	public void MCR(View view) {
		if (!checkClick.isClickEvent()) return;
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		PrinterTypeActivity.setPortName(portNameField.getText().toString());
		PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));

		PrinterFunctions.MCRStart(this);
	}

	public void SampleReceipt(View view) {
		if (!checkClick.isClickEvent()) return;
		final String item_list[] = new String[]{
				getResources().getString(R.string.printArea3inch),
				getResources().getString(R.string.printArea4inch),
		};

		strPrintArea = getResources().getString(R.string.printArea3inch);

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
				PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));
				String commandType = "Line";

				PrinterFunctions.PrintSampleReceipt(me, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings(), commandType, getResources(), strPrintArea);
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
				getResources().getString(R.string.printArea3inch),
				getResources().getString(R.string.printArea4inch),
		};

		strPrintArea = getResources().getString(R.string.printArea3inch);

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
				PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));
				String commandType = "Line";

				PrinterFunctions.PrintSampleReceiptJp(me, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings(), commandType, strPrintArea);
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
		final String item_list[] = new String[]{
				"LAN",
				"Bluetooth",
				"All",
		};

		strInterface = "LAN";

		Builder portDiscoveryDialog = new AlertDialog.Builder(this);
		portDiscoveryDialog.setIcon(android.R.drawable.checkbox_on_background);
		portDiscoveryDialog.setTitle("Port Discovery List");
		portDiscoveryDialog.setCancelable(false);
		portDiscoveryDialog.setSingleChoiceItems(item_list, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				strInterface = item_list[whichButton];
			}
		});

		portDiscoveryDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
				((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);
				if (true == strInterface.equals("LAN")) {
					getPortDiscovery("LAN");
				} else if (strInterface.equals("Bluetooth")) {
					getPortDiscovery("Bluetooth");
				} else {
					getPortDiscovery("All");
				}

			}
		});
		portDiscoveryDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//
			}
		});
		portDiscoveryDialog.show();
	}

	private void InitializeComponent() {    // delete view of some function button for POS Printer Line mode
		TextView titleText = (TextView) findViewById(R.id.textView1);
		titleText.setText("Star Micronics POS Printer Samples");

		Spinner spinner_tcp_port_number = (Spinner) findViewById(R.id.spinner_tcp_port_number);
		SpinnerAdapter ad_tcp_port_number = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Standard", "9100", "9101", "9102", "9103", "9104", "9105", "9106", "9107", "9108", "9109"});
		spinner_tcp_port_number.setAdapter(ad_tcp_port_number);

		Spinner spinner_bluetooth_communication_type = (Spinner) findViewById(R.id.spinner_bluetooth_communication_type);
		SpinnerAdapter ad_bluetooth_communication_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"SSP", "PIN Code"});
		spinner_bluetooth_communication_type.setAdapter(ad_bluetooth_communication_type);

		Spinner spinner_SensorActive = (Spinner) findViewById(R.id.spinner_SensorActive);
		SpinnerAdapter ad_spinner_SensorActive = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"High When Drawer Open", "Low When Drawer Open"});
		spinner_SensorActive.setAdapter(ad_spinner_SensorActive);

		Button MSRButton = (Button) findViewById(R.id.button_MSR);
		MSRButton.setVisibility(View.GONE);

		Button rasterPrintingButton = (Button) findViewById(R.id.button_RasterPrinting);
		rasterPrintingButton.setVisibility(View.GONE);

		Button imagePrintingButton = (Button) findViewById(R.id.button_ImagePrinting);
		imagePrintingButton.setVisibility(View.GONE);
	}

	private String getTCPPortSettings() {
		String portSettings = "";

		Spinner spinner_tcp_port_number = (Spinner) findViewById(R.id.spinner_tcp_port_number);
		switch (spinner_tcp_port_number.getSelectedItemPosition()) {
			case 0:
				portSettings = "";
				break;
			case 1:
				portSettings = "9100";
				break;
			case 2:
				portSettings = "9101";
				break;
			case 3:
				portSettings = "9102";
				break;
			case 4:
				portSettings = "9103";
				break;
			case 5:
				portSettings = "9104";
				break;
			case 6:
				portSettings = "9105";
				break;
			case 7:
				portSettings = "9106";
				break;
			case 8:
				portSettings = "9107";
				break;
			case 9:
				portSettings = "9108";
				break;
			case 10:
				portSettings = "9109";
				break;
		}

		return portSettings;
	}

	private String getBluetoothCommunicationType() {
		String portSettings = "";

		Spinner spinner_bluetooth_communication_type = (Spinner) findViewById(R.id.spinner_bluetooth_communication_type);
		switch (spinner_bluetooth_communication_type.getSelectedItemPosition()) {
			case 0:
				portSettings = "";
				break;
			case 1:
				portSettings = ";p";
				break;
		}

		return portSettings;
	}

	private String getPortSettingsOption(String portName) {
		String portSettings = "";

		if (portName.toUpperCase().startsWith("TCP:")) {
			portSettings += getTCPPortSettings();
		} else if (portName.toUpperCase().startsWith("BT:")) {
			portSettings += getBluetoothCommunicationType();    // Bluetooth option of "portSettings" must be last.
		}

		return portSettings;
	}

	private boolean getSensorActiveHigh() {
		Spinner spinner_SensorActive = (Spinner) findViewById(R.id.spinner_SensorActive);

		switch (spinner_SensorActive.getSelectedItemPosition()) {
			case 0:    // "High"
				return true;
			case 1:    // "Low"
				return false;
			default:
				return true;
		}
	}

	private void getPortDiscovery(String interfaceName) {
		List<PortInfo> BTPortList;
		List<PortInfo> TCPPortList;
		final EditText editPortName;

		final ArrayList<PortInfo> arrayDiscovery;
		ArrayList<String> arrayPortName;

		arrayDiscovery = new ArrayList<PortInfo>();
		arrayPortName = new ArrayList<String>();

		try {
			if (true == interfaceName.equals("Bluetooth") || true == interfaceName.equals("All")) {
				BTPortList = StarIOPort.searchPrinter("BT:");

				for (PortInfo portInfo : BTPortList) {
					arrayDiscovery.add(portInfo);
				}
			}
			if (true == interfaceName.equals("LAN") || true == interfaceName.equals("All")) {
				TCPPortList = StarIOPort.searchPrinter("TCP:");

				for (PortInfo portInfo : TCPPortList) {
					arrayDiscovery.add(portInfo);
				}
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
				.setIcon(android.R.drawable.checkbox_on_background)
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
						editor.putString("portName", portNameField.getText().toString());
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
						editor.putString("portName", portNameField.getText().toString());
						editor.commit();
					}
				})
				.show();
	}
}