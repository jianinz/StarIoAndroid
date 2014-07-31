package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DKAirCashActivity extends Activity {

	private Context me = this;
	private String strInterface = "";
	private String strPrintArea = "";
	private DrawerOpenThread draweropenthread = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.dkaircash);
		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		EditText drawerportNameField = (EditText) findViewById(R.id.editText_DrawerPortName);
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		portNameField.setText(pref.getString("printerportName", "TCP:192.168.192.45"));
		drawerportNameField.setText(pref.getString("drawerportName", "TCP:192.168.192.10"));

		InitializeComponent();
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void InitializeComponent() {    // delete view of some function button for POS Printer Line mode

		Spinner spinner_printerType = (Spinner) findViewById(R.id.spinner_printerType);
		SpinnerAdapter ad_spinner_printerType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"POS Printer", "Portable Printer"});
		spinner_printerType.setAdapter(ad_spinner_printerType);

		Spinner spinner_tcp_port_number = (Spinner) findViewById(R.id.spinner_tcp_port_number);
		SpinnerAdapter ad_tcp_port_number = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Standard", "9100", "9101", "9102", "9103", "9104", "9105", "9106", "9107", "9108", "9109"});
		spinner_tcp_port_number.setAdapter(ad_tcp_port_number);

		Spinner spinner_bluetooth_communication_type = (Spinner) findViewById(R.id.spinner_bluetooth_communication_type);
		SpinnerAdapter ad_bluetooth_communication_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"SSP", "PIN Code"});
		spinner_bluetooth_communication_type.setAdapter(ad_bluetooth_communication_type);

	}

	public void Help(View view) {
		if (!checkClick.isClickEvent()) return;

		Intent myIntent = new Intent(this, helpActivity.class);
		startActivityFromChild(this, myIntent, 0);
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

	private boolean getPrinterType() {
		Spinner spinner_printerType = (Spinner) findViewById(R.id.spinner_printerType);

		switch (spinner_printerType.getSelectedItemPosition()) {
			case 0:    // "POS Printer"
				return true;
			case 1:    // "Portable Printer"
				return false;
			default:
				return true;
		}
	}

	public void PortDiscovery(View view) {
		if (!checkClick.isClickEvent()) return;

		//Check Printer Type
		if (getPrinterType()) {
			//POS printer
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
				}
			});
			portDiscoveryDialog.show();

		} else {
			//Portable printer
			getPortablePortDiscovery(view);
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

					//Check SAC10 model
					if (portInfo.getModelName().startsWith("SAC") == true) {
						arrayDiscovery.remove(portInfo);
					}
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

						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putString("printerportName", portNameField.getText().toString());
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

						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putString("printerportName", portNameField.getText().toString());
						editor.commit();
					}
				})
				.show();
	}

	private void getDrawerPortDiscovery(String interfaceName) {
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

					//Check SAC10 model
					if (portInfo.getModelName().startsWith("SAC") == false) {
						arrayDiscovery.remove(portInfo);
					}

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

						EditText drawerportNameField = (EditText) findViewById(R.id.editText_DrawerPortName);
						drawerportNameField.setText(editPortName.getText());
						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putString("drawerportName", drawerportNameField.getText().toString());
						editor.commit();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int button) {
					}
				})
				.setItems(arrayPortName.toArray(new String[0]), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int select) {
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);

						EditText drawerportNameField = (EditText) findViewById(R.id.editText_DrawerPortName);
						drawerportNameField.setText(arrayDiscovery.get(select).getPortName());

						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putString("drawerportName", drawerportNameField.getText().toString());
						editor.commit();
					}
				})
				.show();
	}

	public void getPortablePortDiscovery(View view) {
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

						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putString("printerportName", portNameField.getText().toString());
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
						EditText drawerportNameField = (EditText) findViewById(R.id.editText_DrawerPortName);

						portNameField.setText(arrayDiscovery.get(select).getPortName());

						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putString("printerportName", portNameField.getText().toString());
						editor.putString("drawerportName", drawerportNameField.getText().toString());
						editor.commit();
					}
				})
				.show();
	}

	public void DrawerPortDiscovery(View view) {

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
					getDrawerPortDiscovery("LAN");
				} else if (strInterface.equals("Bluetooth")) {
					getDrawerPortDiscovery("Bluetooth");
				} else {
					getDrawerPortDiscovery("All");
				}

			}
		});
		portDiscoveryDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		portDiscoveryDialog.show();

	}

	public void GetStatus(View view) {
		if (!checkClick.isClickEvent()) return;

		EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
		String portName = portNameField.getText().toString();
		String portSettings;

		if (getPrinterType()) {
			portSettings = getPortSettingsOption(portName);

			//The portable printer and non portable printer have the same
			CheckStatus(this, portName, portSettings, true);
		} else {
			portSettings = "mini";

			//The portable printer and non portable printer have the same
			MiniPrinterFunctions.CheckStatus(this, portName, portSettings);
		}
	}

	public void SampleReceipt(final View view) {
		if (!checkClick.isClickEvent()) return;

		//showDialog(DIALOG_PRINTABLEAREA_ID);
		final String item_list[];

		if (getPrinterType()) {//POSPrinter
			item_list = new String[]{
					getResources().getString(R.string.printArea3inch),
					getResources().getString(R.string.printArea4inch),
			};

			strPrintArea = getResources().getString(R.string.printArea3inch);

		} else {//PortablePrinter
			item_list = new String[]{
					getResources().getString(R.string.printArea2inch),
					getResources().getString(R.string.printArea3inch),
					getResources().getString(R.string.printArea4inch),
			};

			strPrintArea = getResources().getString(R.string.printArea2inch);

		}

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

				//Printer Port Name
				EditText portNameField = (EditText) findViewById(R.id.editText_PortName);
				PrinterTypeActivity.setPortName(portNameField.getText().toString());

				//Drawer Port Name
				EditText drawerportNameField = (EditText) findViewById(R.id.editText_DrawerPortName);
				String drawerportName = drawerportNameField.getText().toString();
				String drawerportSettings = getPortSettingsOption(drawerportName);

				if (getPrinterType()) {

					draweropenthread = new DrawerOpenThread(drawerportName, drawerportSettings, me);
					draweropenthread.start(); //Start Drawer GetPort Thread

					//POSPrinter
					PrinterTypeActivity.setPortSettings(getPortSettingsOption(PrinterTypeActivity.getPortName()));
					String commandType = "Line";

					PrintSampleReceipt(me, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings(), commandType, getResources(), strPrintArea);

				} else {
					draweropenthread = new DrawerOpenThread(drawerportName, drawerportSettings, me);
					draweropenthread.start(); //Drawer GetPort

					//Portable Printer
					PrinterTypeActivity.setPortSettings("mini");
					MiniPrinterFunctions.PrintSampleReceipt(me, PrinterTypeActivity.getPortName(), PrinterTypeActivity.getPortSettings(), strPrintArea);

				}

				//show password dialog and Kick cash drawer
				kickCashDrawer();

			}
		});
		printableAreaDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		printableAreaDialog.show();

	}

	private void kickCashDrawer() {

		final EditText editView = new EditText(DKAirCashActivity.this);
		editView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		editView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

		//show password dialog 
		new AlertDialog.Builder(DKAirCashActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Please Input Password")
				.setView(editView)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);

						String sText = editView.getText().toString();

						//Password check
						if (sText.matches("1234")) {
							long startTimeMillis = System.currentTimeMillis();
							long endTimeMillis = 0;
							int timeoutMillis = 20000;//20sec
							while (true) {
								//port open check
								if (draweropenthread.portexist() == true) {
									//Drawer fire
									draweropenthread.Fire();
									break;
								}
								try {
									Thread.sleep(20);// interval port open check
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								endTimeMillis = System.currentTimeMillis();
								if ((endTimeMillis - startTimeMillis) > timeoutMillis) {
									//Drawer ReleasePort
									draweropenthread.Release();
									break;
								}
							}
						} else {
							//Drawer ReleasePort
							Builder dialog1 = new AlertDialog.Builder(me);
							dialog1.setNegativeButton("Ok", null);
							AlertDialog alert = dialog1.create();
							alert.setTitle("Failure");
							alert.setMessage("The password is incorrect. stop the process.");
							alert.setCancelable(false);
							alert.show();

							//Drawer ReleasePort
							draweropenthread.Release();
						}
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
						((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);

						//Drawer ReleasePort
						draweropenthread.Release();
					}
				})
				.show();
	}

	public void OpenCashDrawer(View view) {
		if (!checkClick.isClickEvent()) return;

		//Drawer Port Name
		EditText drawerportNameField1 = (EditText) findViewById(R.id.editText_DrawerPortName);
		String drawerportName = drawerportNameField1.getText().toString();
		String drawerportSettings = getPortSettingsOption(drawerportName);

		//Start Drawer GetPort Thread
		draweropenthread = new DrawerOpenThread(drawerportName, drawerportSettings, me);
		draweropenthread.start();

		//show password dialog and Kick cash drawer
		kickCashDrawer();
	}

	public void GetCashDrawerStatus(View view) {

		if (!checkClick.isClickEvent()) return;
		EditText drawerportNameField = (EditText) findViewById(R.id.editText_DrawerPortName);
		String portName = drawerportNameField.getText().toString();
		String portSettings = getPortSettingsOption(portName);

		CheckDrawerStatus(this, portName, portSettings);

	}

	private static byte[] convertFromListByteArrayTobyteArray(List<Byte> ByteArray) {
		byte[] byteArray = new byte[ByteArray.size()];
		for (int index = 0; index < byteArray.length; index++) {
			byteArray[index] = ByteArray.get(index);
		}

		return byteArray;
	}

	private static void sendCommand(Context context, String portName, String portSettings, ArrayList<Byte> byteList) {
		StarIOPort port = null;
		try {
			/*
				using StarIOPort3.1.jar (support USB Port)
				Android OS Version: upper 2.2
			*/
			port = StarIOPort.getPort(portName, portSettings, 10000);
			/* 
				using StarIOPort.jar
				Android OS Version: under 2.1
				port = StarIOPort.getPort(portName, portSettings, 10000);
			*/
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			/*
			   Using Begin / End Checked Block method
               When sending large amounts of raster data,
               adjust the value in the timeout in the "StarIOPort.getPort"
               in order to prevent "timeout" of the "endCheckedBlock method" while a printing.
               
               *If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."),
                 need to change value of timeout more longer in "StarIOPort.getPort" method. (e.g.) 10000 -> 30000
			 */
			StarPrinterStatus status = port.beginCheckedBlock();

			if (true == status.offline) {
				throw new StarIOPortException("A printer is offline");
			}

			byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
			port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

			status = port.endCheckedBlock();

			if (true == status.coverOpen) {
				throw new StarIOPortException("Printer cover is open");
			} else if (true == status.receiptPaperEmpty) {
				throw new StarIOPortException("Receipt paper is empty");
			} else if (true == status.offline) {
				throw new StarIOPortException("Printer is offline");
			}
		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage(e.getMessage());
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function shows how to print the receipt data of a thermal POS printer.
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param commandType  - Command type to use for printing. This should be ("Line" or "Raster")
	 * @param res          - The resources object containing the image data. ( e.g.) getResources())
	 * @param strPrintArea - Printable area size, This should be ("3inch (78mm)" or "4inch (112mm)")
	 */
	public static void PrintSampleReceipt(Context context, String portName, String portSettings, String commandType, Resources res, String strPrintArea) {
		if (commandType == "Line") {
			if (strPrintArea.equals("3inch (78mm)")) {
				byte[] data;
				ArrayList<Byte> list = new ArrayList<Byte>();

				Byte[] tempList;

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x01}));    // Alignment (center)

//	            data = "[If loaded.. Logo1 goes here]\r\n".getBytes();
//	            tempList = new Byte[data.length];
//	            CopyArray(data, tempList);
//	            list.addAll(Arrays.asList(tempList));
//
//	            list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00, '\r', '\n'}));  //Stored Logo Printing

				data = "\nStar Clothing Boutique\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "123 Star Road\r\nCity, State 12345\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x00})); // Alignment

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00})); //Set horizontal tab

				data = "Date: MM/DD/YYYY".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{' ', 0x09, ' '}));   //Moving Horizontal Tab

				data = "Time:HH:MM PM\r\n------------------------------------------------\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45})); // bold

				data = "SALE \r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x46})); // bolf off

				data = "SKU ".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x09}));

				// Notice that we use a unicode representation because that is how Java expresses these bytes as double byte unicode
				// This will TAB to the next horizontal position
				data = "  Description   \u0009         Total\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300678566 \u0009  PLAIN T-SHIRT\u0009         10.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300692003 \u0009  BLACK DENIM\u0009         29.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300651148 \u0009  BLUE DENIM\u0009         29.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300642980 \u0009  STRIPED DRESS\u0009         49.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300638471 \u0009  BLACK BOOTS\u0009         35.99\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Subtotal \u0009\u0009        156.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Tax \u0009\u0009          0.00\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "------------------------------------------------\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Total".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				// Character expansion
				list.addAll(Arrays.asList(new Byte[]{0x06, 0x09, 0x1b, 0x69, 0x01, 0x01}));

				data = "        $156.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x00, 0x00}));  //Cancel Character Expansion

				data = "------------------------------------------------\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Charge\r\n159.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Visa XXXX-XXXX-XXXX-0123\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "\u001b\u0034Refunds and Exchanges\u001b\u0035\r\n".getBytes();  //Specify/Cancel White/Black Invert
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = ("Within " + "\u001b\u002d\u0001" + "30 days\u001b\u002d\u0000" + " with receipt\r\n").getBytes();  //Specify/Cancel Underline Printing
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = ("And tags attached\r\n\r\n").getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				//1D barcode example
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x01}));
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x62, 0x06, 0x02, 0x02}));

				data = (" 12ab34cd56\u001e\r\n").getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x64, 0x02}));    // Cut

				sendCommand(context, portName, portSettings, list);
			} else if (strPrintArea.equals("4inch (112mm)")) {
				byte[] data;
				ArrayList<Byte> list = new ArrayList<Byte>();

				Byte[] tempList;

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x01}));    // Alignment (center)

//	            data = "[If loaded.. Logo1 goes here]\r\n".getBytes();
//	            tempList = new Byte[data.length];
//	            CopyArray(data, tempList);
//	            list.addAll(Arrays.asList(tempList));
//	            list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1c, 0x70, 0x01, 0x00, '\r', '\n'}));  //Stored Logo Printing

				data = "\nStar Clothing Boutique\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "123 Star Road\r\nCity, State 12345\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x00})); // Alignment

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x02, 0x10, 0x22, 0x00})); //Set horizontal tab

				data = "Date: MM/DD/YYYY     \u0009               \u0009       Time:HH:MM PM\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "---------------------------------------------------------------------\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45})); // bold

				data = "SALE \r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x46})); // bolf off

				data = "SKU ".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x09}));

				// Notice that we use a unicode representation because that is how Java expresses these bytes as double byte unicode
				// This will TAB to the next horizontal position
				data = "            Description         \u0009\u0009\u0009                Total\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300678566      \u0009            PLAIN T-SHIRT\u0009                       10.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300692003      \u0009            BLACK DENIM\u0009                         29.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300651148      \u0009            BLUE DENIM\u0009                          29.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300642980      \u0009            STRIPED DRESS\u0009                       49.99\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "300638471      \u0009            BLACK BOOTS\u0009                         35.99\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Subtotal       \u0009                       \u0009                        156.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Tax            \u0009                       \u0009                          0.00\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "---------------------------------------------------------------------\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Total".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				// Character expansion
				list.addAll(Arrays.asList(new Byte[]{0x06, 0x09, 0x1b, 0x69, 0x01, 0x01}));

				data = "\u0009         $156.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x00, 0x00}));  //Cancel Character Expansion

				data = "---------------------------------------------------------------------\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Charge\r\n159.95\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "Visa XXXX-XXXX-XXXX-0123\r\n\r\n".getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = "\u001b\u0034Refunds and Exchanges\u001b\u0035\r\n".getBytes();  //Specify/Cancel White/Black Invert
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = ("Within " + "\u001b\u002d\u0001" + "30 days\u001b\u002d\u0000" + " with receipt\r\n").getBytes();  //Specify/Cancel Underline Printing
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				data = ("And tags attached\r\n\r\n").getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				//1D barcode example
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x01}));
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x62, 0x06, 0x02, 0x02}));

				data = (" 12ab34cd56\u001e\r\n").getBytes();
				tempList = new Byte[data.length];
				CopyArray(data, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x64, 0x02}));    // Cut

				sendCommand(context, portName, portSettings, list);
			}
		} else if (commandType == "Raster") {
		}
	}

	private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
		for (int index = 0; index < cpyArray.length; index++) {
			cpyArray[index] = srcArray[index];
		}
	}

	/**
	 * This function checks the status of the printer
	 *
	 * @param context          - Activity for displaying messages to the user
	 * @param portName         - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings     - Should be blank
	 * @param sensorActiveHigh - boolean variable to tell the sensor active of CashDrawer which is High
	 */
	public static void CheckStatus(Context context, String portName, String portSettings, boolean sensorActiveHigh) {
		StarIOPort port = null;
		try {
			/*
				using StarIOPort3.1.jar (support USB Port)
				Android OS Version: upper 2.2
			*/
			port = StarIOPort.getPort(portName, portSettings, 10000);
			/* 
				using StarIOPort.jar
				Android OS Version: under 2.1
				port = StarIOPort.getPort(portName, portSettings, 10000);
			*/

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			StarPrinterStatus status = port.retreiveStatus();

			if (status.offline == false) {
				String message = "Printer is Online";

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			} else {
				String message = "Printer is Offline";

				if (status.receiptPaperEmpty == true) {
					message += "\nPaper is Empty";
				}

				if (status.coverOpen == true) {
					message += "\nCover is Open";
				}

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.show();
			}

		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}

	/**
	 * This function checks the status of the drawer
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 */
	public static void CheckDrawerStatus(Context context, String portName, String portSettings) {
		StarIOPort port = null;
		try {
			/*
				using StarIOPort3.1.jar (support USB Port)
				Android OS Version: upper 2.2
			*/
			port = StarIOPort.getPort(portName, portSettings, 10000);
			/* 
				using StarIOPort.jar
				Android OS Version: under 2.1
				port = StarIOPort.getPort(portName, portSettings, 10000);
			*/

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			StarPrinterStatus status = port.retreiveStatus();

			if (status.offline == false) {
				String message;

				if (status.compulsionSwitch == false) {
					message = "Cash Drawer: Close";
				} else {
					message = "Cash Drawer: Open";
				}

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Drawer");
				alert.setMessage(message);
				alert.show();
			} else {
				String message;

				if (status.compulsionSwitch == false) {
					message = "Cash Drawer: Close";
				} else {
					message = "Cash Drawer: Open";
				}

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Drawer");
				alert.setMessage(message);
				alert.show();
			}

		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to drawer");
			alert.show();
		} finally {
			if (port != null) {
				try {
					StarIOPort.releasePort(port);
				} catch (StarIOPortException e) {
				}
			}
		}
	}
}

class DrawerOpenThread extends Thread {

	private StarIOPort port1 = null;
	private String portSettings = "";
	private String portName = "";
	private Context me;

	public DrawerOpenThread(String portName, String portSettings, Context context) {

		this.portName = portName;
		this.portSettings = portSettings;
		this.me = context;
	}

	public void run() {
		//GetPort
		try {
			port1 = StarIOPort.getPort(portName, portSettings, 10000);
		} catch (StarIOPortException e) {

			handler.obtainMessage(1).sendToTarget();
		} catch (NullPointerException e1) {
			handler.obtainMessage(2, e1.getMessage()).sendToTarget();
		}
	}

	public boolean portexist() {

		if (port1 != null) {
			return true;
		}
		return false;
	}

	public void Release() {

		if (port1 != null) {
			try {
				//ReleasePort
				StarIOPort.releasePort(port1);
			} catch (StarIOPortException e) {
				handler.obtainMessage(2, e.getMessage()).sendToTarget();
			}

		}

	}

	//DrawerFire
	public void Fire() {
		try {
			//begincheck
			StarPrinterStatus status = port1.beginCheckedBlock();

			if (true == status.offline) {
				String message = "Drawer is offline";
				handler.obtainMessage(2, message).sendToTarget();
			}

			//writePort
			byte[] commands = new byte[]{0x07}; //Drawer open command
			port1.writePort(commands, 0, commands.length);

			//EndCheck
			status = port1.endCheckedBlock();

			if (true == status.offline) {
				String message = "Drawer is offline";
				handler.obtainMessage(2, message).sendToTarget();
			}

		} catch (StarIOPortException e) {

			handler.obtainMessage(2, e.getMessage()).sendToTarget();

		} catch (NullPointerException e1) {
			handler.obtainMessage(2, e1.getMessage()).sendToTarget();
		} finally {
			if (port1 != null) {
				try {
					//ReleasePort
					StarIOPort.releasePort(port1);
				} catch (StarIOPortException e) {
					handler.obtainMessage(2, e.getMessage()).sendToTarget();
				}
			}
		}
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message message) {
			try {
				switch (message.what) {
					case 1:
						Builder dialog = new AlertDialog.Builder(me);
						dialog.setNegativeButton("Ok", null);
						dialog.setTitle("Failure");
						dialog.setMessage("Service discovery failed.");
						AlertDialog alert = dialog.create();
						alert.show();
						break;
					case 2:
						Builder dialog2 = new AlertDialog.Builder(me);
						dialog2.setNegativeButton("Ok", null);
						dialog2.setTitle("Failure");
						dialog2.setMessage(message.obj.toString());
						AlertDialog alert2 = dialog2.create();
						alert2.show();
						break;
				}
			} catch (Exception e) {
			}
		}
	};

}

 