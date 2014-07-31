package com.StarMicronics.StarIOSDK;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.StarMicronics.StarIOSDK.RasterDocument.RasPageEndMode;
import com.StarMicronics.StarIOSDK.RasterDocument.RasSpeed;
import com.StarMicronics.StarIOSDK.RasterDocument.RasTopMargin;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PrinterFunctions {
	public enum NarrowWide {_2_6, _3_9, _4_12, _2_5, _3_8, _4_10, _2_4, _3_6, _4_8}

	;

	public enum BarCodeOption {No_Added_Characters_With_Line_Feed, Adds_Characters_With_Line_Feed, No_Added_Characters_Without_Line_Feed, Adds_Characters_Without_Line_Feed}

	public enum Min_Mod_Size {_2_dots, _3_dots, _4_dots}

	;

	public enum NarrowWideV2 {_2_5, _4_10, _6_15, _2_4, _4_8, _6_12, _2_6, _3_9, _4_12}

	;

	public enum CorrectionLevelOption {Low, Middle, Q, High}

	;

	public enum Model {Model1, Model2}

	;

	public enum Limit {USE_LIMITS, USE_FIXED}

	;

	public enum CutType {FULL_CUT, PARTIAL_CUT, FULL_CUT_FEED, PARTIAL_CUT_FEED}

	;

	public enum Alignment {Left, Center, Right}

	;

	private static int printableArea = 576;    // for raster data

	public static void AddRange(ArrayList<Byte> array, Byte[] newData) {
		for (int index = 0; index < newData.length; index++) {
			array.add(newData[index]);
		}
	}

	/**
	 * This function is used to print a PDF417 barcode to standard Star POS printers
	 *
	 * @param context       - Activity for displaying messages to the user
	 * @param portName      - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings  - Should be blank
	 * @param limit         - Selection of the Method to use specifying the barcode size.  This is either 0 or 1. 0 is Use Limit method and 1 is Use Fixed method. See section 3-122 of the manual (Rev 1.12).
	 * @param p1            - The vertical proportion to use.  The value changes with the limit select.  See section 3-122 of the manual (Rev 1.12).
	 * @param p2            - The horizontal proportion to use.  The value changes with the limit select.  See section 3-122 of the manual (Rev 1.12).
	 * @param securityLevel - This represents how well the barcode can be recovered if it is damaged. This value should be 0 to 8.
	 * @param xDirection    - Specifies the X direction size. This value should be from 1 to 10.  It is recommended that the value be 2 or less.
	 * @param aspectRatio   - Specifies the ratio of the PDF417 barcode.  This values should be from 1 to 10.  It is recommended that this value be 2 or less.
	 * @param barcodeData   - Specifies the characters in the PDF417 barcode.
	 */
	public static void PrintPDF417Code(Context context, String portName, String portSettings, Limit limit, byte p1, byte p2, byte securityLevel, byte xDirection, byte aspectRatio, byte[] barcodeData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] setBarCodeSize = new Byte[]{0x1b, 0x1d, 0x78, 0x53, 0x30, 0x00, 0x00, 0x00};
		switch (limit) {
			case USE_LIMITS:
				setBarCodeSize[5] = 0;
				break;
			case USE_FIXED:
				setBarCodeSize[5] = 1;
				break;
		}

		setBarCodeSize[6] = p1;
		setBarCodeSize[7] = p2;
		AddRange(commands, setBarCodeSize);

		Byte[] setSecurityLevel = new Byte[]{0x1b, 0x1d, 0x78, 0x53, 0x31, 0x00};
		setSecurityLevel[5] = securityLevel;
		AddRange(commands, setSecurityLevel);

		Byte[] setXDirections = new Byte[]{0x1b, 0x1d, 0x78, 0x53, 0x32, 0x00};
		setXDirections[5] = xDirection;
		AddRange(commands, setXDirections);

		Byte[] setAspectRatio = new Byte[]{0x1b, 0x1d, 0x78, 0x53, 0x33, 0x00};
		setAspectRatio[5] = aspectRatio;
		AddRange(commands, setAspectRatio);

		Byte[] setBarcodeData = new Byte[6 + barcodeData.length];
		setBarcodeData[0] = 0x1b;
		setBarcodeData[1] = 0x1d;
		setBarcodeData[2] = 0x78;
		setBarcodeData[3] = 0x44;
		setBarcodeData[4] = (byte) (barcodeData.length % 256);
		setBarcodeData[5] = (byte) (barcodeData.length / 256);
		for (int index = 0; index < barcodeData.length; index++) {
			setBarcodeData[index + 6] = barcodeData[index];
		}
		AddRange(commands, setBarcodeData);

		Byte[] printBarcode = new Byte[]{0x1b, 0x1d, 0x78, 0x50};
		AddRange(commands, printBarcode);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print a QR Code on standard Star POS printers
	 *
	 * @param context         - Activity for displaying messages to the user
	 * @param portName        - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings    - Should be blank
	 * @param correctionLevel - The correction level for the QR Code.  The correction level can be 7, 15, 25, or 30.  See section 3-129 (Rev. 1.12).
	 * @param model           - The model to use when printing the QR Code. See section 3-129 (Rev. 1.12).
	 * @param cellSize        - The cell size of the QR Code.  The value of this should be between 1 and 8. It is recommended that this value is 2 or less.
	 * @param barCodeData     - Specifies the characters in the QR Code.
	 */
	public static void PrintQrCode(Context context, String portName, String portSettings, CorrectionLevelOption correctionLevel, Model model, byte cellSize, byte[] barCodeData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] modelCommand = new Byte[]{0x1b, 0x1d, 0x79, 0x53, 0x30, 0x00};
		switch (model) {
			case Model1:
				modelCommand[5] = 1;
				break;
			case Model2:
				modelCommand[5] = 2;
				break;
		}

		AddRange(commands, modelCommand);

		Byte[] correctionLevelCommand = new Byte[]{0x1b, 0x1d, 0x79, 0x53, 0x31, 0x00};
		switch (correctionLevel) {
			case Low:
				correctionLevelCommand[5] = 0;
				break;
			case Middle:
				correctionLevelCommand[5] = 1;
				break;
			case Q:
				correctionLevelCommand[5] = 2;
				break;
			case High:
				correctionLevelCommand[5] = 3;
				break;
		}
		AddRange(commands, correctionLevelCommand);

		Byte[] cellCodeSize = new Byte[]{0x1b, 0x1d, 0x79, 0x53, 0x32, cellSize};
		AddRange(commands, cellCodeSize);

		//Add BarCode data
		AddRange(commands, new Byte[]{0x1b, 0x1d, 0x79, 0x44, 0x31, 0x00});
		commands.add((byte) (barCodeData.length % 256));
		commands.add((byte) (barCodeData.length / 256));

		for (int index = 0; index < barCodeData.length; index++) {
			commands.add(barCodeData[index]);
		}

		Byte[] printQrCodeCommand = new Byte[]{0x1b, 0x1d, 0x79, 0x50};
		AddRange(commands, printQrCodeCommand);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function opens the cash drawer connected to the printer
	 * This function just send the byte 0x07 to the printer which is the open cashdrawer command
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 */
	public static void OpenCashDrawer(Context context, String portName, String portSettings) {
		ArrayList<Byte> commands = new ArrayList<Byte>();
		byte openCashDrawer = 0x07;

		commands.add(openCashDrawer);

		sendCommand(context, portName, portSettings, commands);
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

				if (status.compulsionSwitch == false) {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Close";
					} else {
						message += "\nCash Drawer: Open";
					}
				} else {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Open";
					} else {
						message += "\nCash Drawer: Close";
					}
				}

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

				if (status.compulsionSwitch == false) {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Close";
					} else {
						message += "\nCash Drawer: Open";
					}
				} else {
					if (true == sensorActiveHigh) {
						message += "\nCash Drawer: Open";
					} else {
						message += "\nCash Drawer: Close";
					}
				}

				Builder dialog = new AlertDialog.Builder(context);
				dialog.setNegativeButton("Ok", null);
				AlertDialog alert = dialog.create();
				alert.setTitle("Printer");
				alert.setMessage(message);
				alert.setCancelable(false);
				alert.show();
			}

		} catch (StarIOPortException e) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setNegativeButton("Ok", null);
			AlertDialog alert = dialog.create();
			alert.setTitle("Failure");
			alert.setMessage("Failed to connect to printer");
			alert.setCancelable(false);
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
	 * This function is used to print barcodes in 39 format
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param barcodeData  - These are the characters that will be printed in the barcode. The characters available for this bar code are listed in section 3-43 (Rev. 1.12).
	 * @param option       - This tells the printer to put characters under the printed barcode or not.  This may also be used to line feed after the barcode is printed.
	 * @param height       - The height of the barcode.  This is measured in pixels
	 * @param width        - The width of the barcode.  This value should be between 1 to 9.  See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCode39(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, NarrowWide width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x34;
		byte n2 = 0;
		switch (option) {
			case No_Added_Characters_With_Line_Feed:
				n2 = 49;
				break;
			case Adds_Characters_With_Line_Feed:
				n2 = 50;
				break;
			case No_Added_Characters_Without_Line_Feed:
				n2 = 51;
				break;
			case Adds_Characters_Without_Line_Feed:
				n2 = 52;
				break;
		}
		byte n3 = 0;
		switch (width) {
			case _2_6:
				n3 = 49;
				break;
			case _3_9:
				n3 = 50;
				break;
			case _4_12:
				n3 = 51;
				break;
			case _2_5:
				n3 = 52;
				break;
			case _3_8:
				n3 = 53;
				break;
			case _4_10:
				n3 = 54;
				break;
			case _2_4:
				n3 = 55;
				break;
			case _3_6:
				n3 = 56;
				break;
			case _4_8:
				n3 = 57;
				break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in 93 format
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param barcodeData  - These are the characters that will be printed in the barcode. The characters available for this barcode are listed in section 3-43 (Rev. 1.12).
	 * @param option       - This tells the printer to put characters under the printed barcode or not.  This may also be used to line feed after the barcode is printed.
	 * @param height       - The height of the barcode.  This is measured in pixels
	 * @param width        - This is the number of dots per module.  This value should be between 1 to 3.  See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCode93(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, Min_Mod_Size width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x37;
		byte n2 = 0;
		switch (option) {
			case No_Added_Characters_With_Line_Feed:
				n2 = 49;
				break;
			case Adds_Characters_With_Line_Feed:
				n2 = 50;
				break;
			case No_Added_Characters_Without_Line_Feed:
				n2 = 51;
				break;
			case Adds_Characters_Without_Line_Feed:
				n2 = 52;
				break;
		}
		byte n3 = 0;
		switch (width) {
			case _2_dots:
				n3 = 49;
				break;
			case _3_dots:
				n3 = 50;
				break;
			case _4_dots:
				n3 = 51;
				break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in ITF format
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param barcodeData  - These are the characters that will be printed in the barcode. The characters available for this barcode are listed in section 3-43 (Rev. 1.12).
	 * @param option       - This tell the printer to put characters under the printed barcode or not.  This may also be used to line feed after the barcode is printed.
	 * @param height       - The height of the barcode.  This is measured in pixels
	 * @param width        - The width of the barcode.  This value should be between 1 to 9.  See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCodeITF(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, NarrowWideV2 width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x35;
		byte n2 = 0;
		switch (option) {
			case No_Added_Characters_With_Line_Feed:
				n2 = 49;
				break;
			case Adds_Characters_With_Line_Feed:
				n2 = 50;
				break;
			case No_Added_Characters_Without_Line_Feed:
				n2 = 51;
				break;
			case Adds_Characters_Without_Line_Feed:
				n2 = 52;
				break;
		}
		byte n3 = 0;
		switch (width) {
			case _2_5:
				n3 = 49;
				break;
			case _4_10:
				n3 = 50;
				break;
			case _6_15:
				n3 = 51;
				break;
			case _2_4:
				n3 = 52;
				break;
			case _4_8:
				n3 = 53;
				break;
			case _6_12:
				n3 = 54;
				break;
			case _2_6:
				n3 = 55;
				break;
			case _3_9:
				n3 = 56;
				break;
			case _4_12:
				n3 = 57;
				break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print barcodes in the 128 format
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param barcodeData  - These are the characters that will be printed in the barcode. The characters available for this barcode are listed in section 3-43 (Rev. 1.12).
	 * @param option       - This tell the printer to put characters under the printed barcode or not.  This may also be used to line feed after the barcode is printed.
	 * @param height       - The height of the barcode.  This is measured in pixels
	 * @param width        - This is the number of dots per module.  This value should be between 1 to 3.  See section 3-42 (Rev. 1.12) for more information on the values.
	 */
	public static void PrintCode128(Context context, String portName, String portSettings, byte[] barcodeData, BarCodeOption option, byte height, Min_Mod_Size width) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		byte n1 = 0x36;
		byte n2 = 0;
		switch (option) {
			case No_Added_Characters_With_Line_Feed:
				n2 = 49;
				break;
			case Adds_Characters_With_Line_Feed:
				n2 = 50;
				break;
			case No_Added_Characters_Without_Line_Feed:
				n2 = 51;
				break;
			case Adds_Characters_Without_Line_Feed:
				n2 = 52;
				break;
		}
		byte n3 = 0;
		switch (width) {
			case _2_dots:
				n3 = 49;
				break;
			case _3_dots:
				n3 = 50;
				break;
			case _4_dots:
				n3 = 51;
				break;
		}
		byte n4 = height;
		Byte[] command = new Byte[6 + barcodeData.length + 1];
		command[0] = 0x1b;
		command[1] = 0x62;
		command[2] = n1;
		command[3] = n2;
		command[4] = n3;
		command[5] = n4;
		for (int index = 0; index < barcodeData.length; index++) {
			command[index + 6] = barcodeData[index];
		}
		command[command.length - 1] = 0x1e;

		AddRange(commands, command);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function shows different cut patterns for Star POS printers.
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param cuttype      - The cut type to perform. The cut types are full cut, full cut with feed, partial cut, and partial cut with feed
	 */
	public static void performCut(Context context, String portName, String portSettings, CutType cuttype) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] autocutCommand = new Byte[]{0x1b, 0x64, 0x00};
		switch (cuttype) {
			case FULL_CUT:
				autocutCommand[2] = 48;
				break;
			case PARTIAL_CUT:
				autocutCommand[2] = 49;
				break;
			case FULL_CUT_FEED:
				autocutCommand[2] = 50;
				break;
			case PARTIAL_CUT_FEED:
				autocutCommand[2] = 51;
				break;
		}

		AddRange(commands, autocutCommand);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw text to the printer, showing how the text can be formated.  Ex: Changing size
	 *
	 * @param context         - Activity for displaying messages to the user
	 * @param portName        - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings    - Should be blank
	 * @param slashedZero     - boolean variable to tell the printer to slash zeroes
	 * @param underline       - boolean variable that tells the printer to underline the text
	 * @param invertColor     - boolean variable that tells the printer to should invert text.  All white space will become black but the characters will be left white.
	 * @param emphasized      - boolean variable that tells the printer to should emphasize the printed text.  This is somewhat like bold. It isn't as dark, but darker than regular characters.
	 * @param upperline       - boolean variable that tells the printer to place a line above the text.  This is only supported by newest printers.
	 * @param upsideDown      - boolean variable that tells the printer to print text upside down.
	 * @param heightExpansion - This integer tells the printer what the character height should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param widthExpansion  - This integer tell the printer what the character width should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param leftMargin      - Defines the left margin for text on Star portable printers.  This number can be from 0 to 65536. However, remember how much space is available as the text can be pushed off the page.
	 * @param alignment       - Defines the alignment of the text. The printers support left, right, and center justification.
	 * @param textData        - The text to send to the printer.
	 */
	public static void PrintText(Context context, String portName, String portSettings, boolean slashedZero, boolean underline, boolean invertColor, boolean emphasized, boolean upperline, boolean upsideDown, int heightExpansion, int widthExpansion, byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[]{0x1b, 0x40};    // Initialization
		AddRange(commands, initCommand);

		Byte[] slashedZeroCommand = new Byte[]{0x1b, 0x2f, 0x00};
		if (slashedZero) {
			slashedZeroCommand[2] = 49;
		} else {
			slashedZeroCommand[2] = 48;
		}
		AddRange(commands, slashedZeroCommand);

		Byte[] underlineCommand = new Byte[]{0x1b, 0x2d, 0x00};
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] invertColorCommand = new Byte[]{0x1b, 0x00};
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		AddRange(commands, invertColorCommand);

		Byte[] emphasizedPrinting = new Byte[]{0x1b, 0x00};
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[]{0x1b, 0x5f, 0x00};
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterExpansion = new Byte[]{0x1b, 0x69, 0x00, 0x00};
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		AddRange(commands, characterExpansion);

		Byte[] leftMarginCommand = new Byte[]{0x1b, 0x6c, 0x00};
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[]{0x1b, 0x1d, 0x61, 0x00};
		switch (alignment) {
			case Left:
				alignmentCommand[3] = 48;
				break;
			case Center:
				alignmentCommand[3] = 49;
				break;
			case Right:
				alignmentCommand[3] = 50;
				break;
		}
		AddRange(commands, alignmentCommand);

		for (int i = 0; i < textData.length; i++) {
			commands.add(textData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function sends raw JP-Kanji text to the printer, showing how the text can be formated.  Ex: Changing size
	 *
	 * @param context         - Activity for displaying messages to the user
	 * @param portName        - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings    - Should be blank
	 * @param shiftJIS        - boolean variable to tell the printer to Shift-JIS/JIS
	 * @param underline       - boolean variable that tells the printer to underline the text
	 * @param invertColor     - boolean variable that tells the printer to should invert text.  All white space will become black but the characters will be left white.
	 * @param emphasized      - boolean variable that tells the printer to should emphasize the printed text.  This is somewhat like bold. It isn't as dark, but darker than regular characters.
	 * @param upperline       - boolean variable that tells the printer to place a line above the text.  This is only supported by newest printers.
	 * @param upsideDown      - boolean variable that tells the printer to print text upside down.
	 * @param heightExpansion - This integer tells the printer what the character height should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param widthExpansion  - This integer tell the printer what the character width should be, ranging from 0 to 5 and representing multiples from 1 to 6.
	 * @param leftMargin      - Defines the left margin for text on Star portable printers.  This number can be from 0 to 65536. However, remember how much space is available as the text can be pushed off the page.
	 * @param alignment       - Defines the alignment of the text. The printers support left, right, and center justification.
	 * @param textData        - The text to send to the printer.
	 */
	public static void PrintTextKanji(Context context, String portName, String portSettings, boolean shiftJIS, boolean underline, boolean invertColor, boolean emphasized, boolean upperline, boolean upsideDown, int heightExpansion, int widthExpansion, byte leftMargin, Alignment alignment, byte[] textData) {
		ArrayList<Byte> commands = new ArrayList<Byte>();

		Byte[] initCommand = new Byte[]{0x1b, 0x40};    // Initialization
		AddRange(commands, initCommand);

		if (shiftJIS) {
			Byte[] kanjiModeCommand = new Byte[]{0x1b, 0x71, 0x1b, 0x24, 0x31};    // Shift-JIS Kanji Mode(Disable JIS + Enable Shift-JIS)
			AddRange(commands, kanjiModeCommand);
		} else {
			Byte[] kanjiModeCommand = new Byte[]{0x1b, 0x24, 0x30, 0x1b, 0x70};    // JIS Kanji Mode(Disable Shift-JIS + Enable JIS)
			AddRange(commands, kanjiModeCommand);
		}

		Byte[] underlineCommand = new Byte[]{0x1b, 0x2d, 0x00};
		if (underline) {
			underlineCommand[2] = 49;
		} else {
			underlineCommand[2] = 48;
		}
		AddRange(commands, underlineCommand);

		Byte[] invertColorCommand = new Byte[]{0x1b, 0x00};
		if (invertColor) {
			invertColorCommand[1] = 0x34;
		} else {
			invertColorCommand[1] = 0x35;
		}
		AddRange(commands, invertColorCommand);

		Byte[] emphasizedPrinting = new Byte[]{0x1b, 0x00};
		if (emphasized) {
			emphasizedPrinting[1] = 69;
		} else {
			emphasizedPrinting[1] = 70;
		}
		AddRange(commands, emphasizedPrinting);

		Byte[] upperLineCommand = new Byte[]{0x1b, 0x5f, 0x00};
		if (upperline) {
			upperLineCommand[2] = 49;
		} else {
			upperLineCommand[2] = 48;
		}
		AddRange(commands, upperLineCommand);

		if (upsideDown) {
			commands.add((byte) 0x0f);
		} else {
			commands.add((byte) 0x12);
		}

		Byte[] characterExpansion = new Byte[]{0x1b, 0x69, 0x00, 0x00};
		characterExpansion[2] = (byte) (heightExpansion + '0');
		characterExpansion[3] = (byte) (widthExpansion + '0');
		AddRange(commands, characterExpansion);

		Byte[] leftMarginCommand = new Byte[]{0x1b, 0x6c, 0x00};
		leftMarginCommand[2] = leftMargin;
		AddRange(commands, leftMarginCommand);

		Byte[] alignmentCommand = new Byte[]{0x1b, 0x1d, 0x61, 0x00};
		switch (alignment) {
			case Left:
				alignmentCommand[3] = 48;
				break;
			case Center:
				alignmentCommand[3] = 49;
				break;
			case Right:
				alignmentCommand[3] = 50;
				break;
		}
		AddRange(commands, alignmentCommand);

		// textData Encoding!!
		String strData = new String(textData);
		byte[] rawData = null;
		try {
			if (shiftJIS) {
				rawData = strData.getBytes("Shift_JIS");    // Shift JIS code
			} else {
				rawData = strData.getBytes("ISO2022JP");    // JIS code
			}
		} catch (UnsupportedEncodingException e) {
			rawData = strData.getBytes();
		}

		for (int i = 0; i < rawData.length; i++) {
			commands.add(rawData[i]);
		}

		commands.add((byte) 0x0a);

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print a Java bitmap directly to the printer.
	 * There are 2 ways a printer can print images: through raster commands or line mode commands
	 * This function uses raster commands to print an image.  Raster is supported on the TSP100 and all Star Thermal POS printers.
	 * Line mode printing is not supported by the TSP100. There is no example of using this method in this sample.
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param source       - The bitmap to convert to Star Raster data
	 * @param maxWidth     - The maximum width of the image to print.  This is usually the page width of the printer.  If the image exceeds the maximum width then the image is scaled down.  The ratio is maintained.
	 */
	public static void PrintBitmap(Context context, String portName, String portSettings, Bitmap source, int maxWidth, boolean compressionEnable) {
		ArrayList<Byte> commands = new ArrayList<Byte>();
		Byte[] tempList;

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
		StarBitmap starbitmap = new StarBitmap(source, false, maxWidth);

		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		command = starbitmap.getImageRasterDataForPrinting(compressionEnable);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * This function is used to print a Java bitmap directly to the printer.
	 * There are 2 ways a printer can print images: through raster commands or line mode commands
	 * This function uses raster commands to print an image.  Raster is supported on the TSP100 and all Star Thermal POS printers.
	 * Line mode printing is not supported by the TSP100. There is no example of using this method in this sample.
	 *
	 * @param context      - Activity for displaying messages to the user
	 * @param portName     - Port name to use for communication. This should be (TCP:<IPAddress>)
	 * @param portSettings - Should be blank
	 * @param res          - The resources object containing the image data
	 * @param source       - The resource id of the image data
	 * @param maxWidth     - The maximum width of the image to print.  This is usually the page width of the printer.  If the image exceeds the maximum width then the image is scaled down.  The ratio is maintained.
	 */
	public static void PrintBitmapImage(Context context, String portName, String portSettings, Resources res, int source, int maxWidth, boolean compressionEnable) {
		ArrayList<Byte> commands = new ArrayList<Byte>();
		Byte[] tempList;

		RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
		Bitmap bm = BitmapFactory.decodeResource(res, source);
		StarBitmap starbitmap = new StarBitmap(bm, false, maxWidth);

		byte[] command = rasterDoc.BeginDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		command = starbitmap.getImageRasterDataForPrinting(compressionEnable);
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		command = rasterDoc.EndDocumentCommandData();
		tempList = new Byte[command.length];
		CopyArray(command, tempList);
		commands.addAll(Arrays.asList(tempList));

		sendCommand(context, portName, portSettings, commands);
	}

	/**
	 * MSR functionality is supported on Star portable printers only.
	 *
	 * @param context - Activity for displaying messages to the user that this function is not supported
	 */
	public static void MCRStart(Context context) {
		Builder dialog = new AlertDialog.Builder(context);
		dialog.setNegativeButton("Ok", null);
		AlertDialog alert = dialog.create();
		alert.setTitle("Feature Not Available");
		alert.setMessage("MSR functionality is supported only on portable printer models");
		alert.setCancelable(false);
		alert.show();
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
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

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
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
			}
		} else if (commandType == "Raster") {
			if (strPrintArea.equals("3inch (78mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 576;    // Printable area in paper is 832(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				String textToPrint = ("                       Star Clothing Boutique\r\n" +
						"                             123 Star Road\r\n" +
						"                           City, State 12345\r\n\r\n" +
						"Date: MM/DD/YYYY                 Time:HH:MM PM\r\n" +
						"-----------------------------------------------------------------------\r");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("SALE");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("SKU \t\t\t                 Description \t\t                Total\r\n" +
						"300678566 \t\t\t      PLAIN T-SHIRT		\t\t    10.99\n" +
						"300692003 \t\t\t      BLACK DENIM		\t\t    29.99\n" +
						"300651148 \t\t\t      BLUE DENIM		\t\t       29.99\n" +
						"300642980 \t\t\t      STRIPED DRESS		\t       49.99\n" +
						"300638471 \t\t\t      BLACK BOOTS		\t\t       35.99\n\n" +
						"Subtotal\t\t\t\t                                              156.95\r\n" +
						"Tax		\t\t\t\t                                                     0.00\r\n" +
						"-----------------------------------------------------------------------\r\n" +
						"Total   \t                                                   $156.95\r\n" +
						"-----------------------------------------------------------------------\r\n\r\n" +
						"Charge\r\n159.95\r\n" +
						"Visa XXXX-XXXX-XXXX-0123\r\n");

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Refunds and Exchanges");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Within 30 days with receipt\r\n" +
						"And tags attached");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.qrcode);
				StarBitmap starbitmap = new StarBitmap(bm, false, 146);
				command = starbitmap.getImageRasterDataForPrinting(true);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
			} else if (strPrintArea.equals("4inch (112mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 832;    // Printable area in paper is 832(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				String textToPrint = ("                                          Star Clothing Boutique\r\n" +
						"                                                123 Star Road\r\n" +
						"                                              City, State 12345\r\n\r\n" +
						"Date: MM/DD/YYYY                                                      Time:HH:MM PM\r\n" +
						"-------------------------------------------------------------------------------------------------------\r");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("SALE");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("SKU \t\t\t                                   Description \t\t                                  Total\r\n" +
						"300678566 \t\t\t                        PLAIN T-SHIRT		\t\t                      10.99\n" +
						"300692003 \t\t\t                        BLACK DENIM		\t\t                      29.99\n" +
						"300651148 \t\t\t                        BLUE DENIM		\t\t                         29.99\n" +
						"300642980 \t\t\t                        STRIPED DRESS		\t                         49.99\n" +
						"300638471 \t\t\t                        BLACK BOOTS		\t\t                      35.99\n\n");

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Subtotal\t\t\t\t                                                                                  156.95\r\n" +
						"Tax		\t\t\t\t                                                                                         0.00\r\n" +
						"-------------------------------------------------------------------------------------------------------\r" +
						"Total   \t                                                                                       $156.95\r\n" +
						"-------------------------------------------------------------------------------------------------------\r\n\r\n" +
						"Charge\r\n159.95\r\n" +
						"Visa XXXX-XXXX-XXXX-0123\r\n");

				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Refunds and Exchanges");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("Within 30 days with receipt\r\n" +
						"And tags attached");
				command = createRasterCommand(textToPrint, 13, 0);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.qrcode);
				StarBitmap starbitmap = new StarBitmap(bm, false, 146);
				command = starbitmap.getImageRasterDataForPrinting(true);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
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
	 * @param strPrintArea - Printable area size, This should be ("3inch (78mm)" or "4inch (112mm)")
	 */
	public static void PrintSampleReceiptJp(Context context, String portName, String portSettings, String commandType, String strPrintArea) {
		if ("Line" == commandType) {
			if (strPrintArea.equals("3inch (78mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				byte[] outputByteBuffer = null;
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x40}));                         // Initialization
//				list.addAll(Arrays.asList(new Byte[]{0x1d, 0x57, (byte) 0x80, 0x01}));      // 印字領域設定（58mm用紙）
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x24, 0x31}));                   // 漢字モード設定
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x10, 0x00}));             // 水平タブ位置設定
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x31}));             // 中央揃え設定

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x02, 0x00}));             // 文字縦拡大設定
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45}));                         // 強調印字設定

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.title_company_name) + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x01, 0x00}));             // 文字縦拡大設定

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.title_receipt_name) + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x00, 0x00}));             // 文字縦拡大解除
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x46}));                         // 強調印字解除

				outputByteBuffer = createShiftJIS("------------------------------------------------\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String YMD = (year + context.getResources().getString(R.string.year) +
						(month + 1) + context.getResources().getString(R.string.month) +
						day + context.getResources().getString(R.string.day)).toString();

				int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				String TIME = (hour24 + context.getResources().getString(R.string.hour) +
						minute + context.getResources().getString(R.string.min)).toString();

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x30}));            //左揃え設定

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.date) + YMD + "  " + TIME + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS("TEL:054-347-XXXX\n\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.kana_line) + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.personalInfo));
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.ItemInfo_3inch_line));
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				int sub = 0;    //小計
				int tax = 0;    //税額
				sub = 10000 + 3800 + 2000 + 15000 + 5000;
				NumberFormat exsub = NumberFormat.getNumberInstance();

				tax = sub * 5 / 100;
				NumberFormat extax = NumberFormat.getNumberInstance();

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.sub_3inch_line) + exsub.format(sub) + "\n\n" +
						context.getResources().getString(R.string.tax_3inch_line) + extax.format(tax) + "\n\n" +
						context.getResources().getString(R.string.total_3inch_line) + exsub.format(sub) + "\n\n" +
						context.getResources().getString(R.string.phone) + "\n\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x64, 0x33}));    // Cut
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
			} else if (strPrintArea.equals("4inch (112mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				byte[] outputByteBuffer = null;
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x40}));                         // Initialization
//				list.addAll(Arrays.asList(new Byte[]{0x1d, 0x57, (byte) 0x80, 0x01}));      // 印字領域設定（58mm用紙）
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x24, 0x31}));                   // 漢字モード設定
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x44, 0x10, 0x00}));             // 水平タブ位置設定
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x31}));             // 中央揃え設定

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x02, 0x00}));             // 文字縦拡大設定
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x45}));                         // 強調印字設定

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.title_company_name) + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x01, 0x00}));             // 文字縦拡大設定

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.title_receipt_name) + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x69, 0x00, 0x00}));             // 文字縦拡大解除
				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x46}));                         // 強調印字解除

				outputByteBuffer = createShiftJIS("--------------------------------------------------------------------\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String YMD = (year + context.getResources().getString(R.string.year) +
						(month + 1) + context.getResources().getString(R.string.month) +
						day + context.getResources().getString(R.string.day)).toString();

				int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				String TIME = (hour24 + context.getResources().getString(R.string.hour) +
						minute + context.getResources().getString(R.string.min)).toString();

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x1d, 0x61, 0x30}));            //左揃え設定

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.date) + YMD + "  " + TIME + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS("TEL:054-347-XXXX\n\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.kana_line) + "\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.personalInfo));
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.ItemInfo_4inch_line));
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				int sub = 0;    //小計
				int tax = 0;    //税額
				sub = 10000 + 3800 + 2000 + 15000 + 5000;
				NumberFormat exsub = NumberFormat.getNumberInstance();

				tax = sub * 5 / 100;
				NumberFormat extax = NumberFormat.getNumberInstance();

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x52, 0x08}));    // 国際文字 日本語

				outputByteBuffer = createShiftJIS(context.getResources().getString(R.string.sub_4inch_line) + exsub.format(sub) + "\n\n" +
						context.getResources().getString(R.string.tax_4inch_line) + extax.format(tax) + "\n\n" +
						context.getResources().getString(R.string.total_4inch_line) + exsub.format(sub) + "\n\n" +
						context.getResources().getString(R.string.phone) + "\n\n");
				tempList = new Byte[outputByteBuffer.length];
				CopyArray(outputByteBuffer, tempList);
				list.addAll(Arrays.asList(tempList));

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x64, 0x33}));    // カット
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
			}
		} else if ("Raster" == commandType) {
			if (strPrintArea.equals("3inch (78mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 576;    // Printable area in paper is 576(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				String textToPrint = (context.getResources().getString(R.string.title_company_name_raster_3inch) +
						context.getResources().getString(R.string.title_receipt_name_raster_3inch));
				command = createRasterCommand(textToPrint, 21, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("-----------------------------------------------------------------------");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String YMD = (year + context.getResources().getString(R.string.year) +
						(month + 1) + context.getResources().getString(R.string.month) +
						day + context.getResources().getString(R.string.day)).toString();

				int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				String TIME = (hour24 + context.getResources().getString(R.string.hour) +
						minute + context.getResources().getString(R.string.min)).toString();

				textToPrint = (context.getResources().getString(R.string.date) + YMD + "  " + TIME + "\n" +
						"TEL:054-347-XXXX\n\n");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = (context.getResources().getString(R.string.kana_raster) + "\n" +
						context.getResources().getString(R.string.personalInfo));
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = context.getResources().getString(R.string.ItemInfo_3inch_raster);
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				int sub = 0;    //小計
				int tax = 0;    //税額
				sub = 10000 + 3800 + 2000 + 15000 + 5000;
				NumberFormat exsub = NumberFormat.getNumberInstance();

				tax = sub * 5 / 100;
				NumberFormat extax = NumberFormat.getNumberInstance();

				textToPrint = (context.getResources().getString(R.string.sub_3inch_raster) + exsub.format(sub) + "\n" +
						context.getResources().getString(R.string.tax_3inch_raster) + extax.format(tax) + "\n" +
						context.getResources().getString(R.string.total_3inch_raster) + exsub.format(sub) + "\n\n" +
						context.getResources().getString(R.string.phone) + "\n\n");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
			} else if (strPrintArea.equals("4inch (112mm)")) {
				ArrayList<Byte> list = new ArrayList<Byte>();
				Byte[] tempList;

				printableArea = 832;    // Printable area in paper is 832(dot)

				RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
				byte[] command = rasterDoc.BeginDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				String textToPrint = (context.getResources().getString(R.string.title_company_name_raster_4inch) +
						context.getResources().getString(R.string.title_receipt_name_raster_4inch));
				command = createRasterCommand(textToPrint, 21, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = ("------------------------------------------------------------------------------------------------------");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				String YMD = (year + context.getResources().getString(R.string.year) +
						(month + 1) + context.getResources().getString(R.string.month) +
						day + context.getResources().getString(R.string.day)).toString();

				int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				String TIME = (hour24 + context.getResources().getString(R.string.hour) +
						minute + context.getResources().getString(R.string.min)).toString();

				textToPrint = (context.getResources().getString(R.string.date) + YMD + "  " + TIME + "\n" +
						"TEL:054-347-XXXX\n\n");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = (context.getResources().getString(R.string.kana_raster) + "\n" +
						context.getResources().getString(R.string.personalInfo));
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				textToPrint = context.getResources().getString(R.string.ItemInfo_4inch_raster);
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				int sub = 0;    //小計
				int tax = 0;    //税額
				sub = 10000 + 3800 + 2000 + 15000 + 5000;
				NumberFormat exsub = NumberFormat.getNumberInstance();

				tax = sub * 5 / 100;
				NumberFormat extax = NumberFormat.getNumberInstance();

				list.addAll(Arrays.asList(new Byte[]{0x1b, 0x52, 0x08}));    // 国際文字 日本語

				textToPrint = (context.getResources().getString(R.string.sub_4inch_raster) + exsub.format(sub) + "\n" +
						context.getResources().getString(R.string.tax_4inch_raster) + extax.format(tax) + "\n" +
						context.getResources().getString(R.string.total_4inch_raster) + exsub.format(sub) + "\n\n" +
						context.getResources().getString(R.string.phone) + "\n\n\n\n");
				command = createRasterCommand(textToPrint, 13, Typeface.BOLD);
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));

				command = rasterDoc.EndDocumentCommandData();
				tempList = new Byte[command.length];
				CopyArray(command, tempList);
				list.addAll(Arrays.asList(tempList));
				list.addAll(Arrays.asList(new Byte[]{0x07}));                // Kick cash drawer

				sendCommand(context, portName, portSettings, list);
			}
		}
	}

	private static byte[] createShiftJIS(String inputText) {
		byte[] byteBuffer = null;

		try {
			byteBuffer = inputText.getBytes("Shift_JIS");
		} catch (UnsupportedEncodingException e) {
			byteBuffer = inputText.getBytes();
		}

		return byteBuffer;
	}

	private static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
		for (int index = 0; index < cpyArray.length; index++) {
			cpyArray[index] = srcArray[index];
		}
	}

	private static byte[] createRasterCommand(String printText, int textSize, int bold) {
		byte[] command;

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);

		Typeface typeface;

		try {
			typeface = Typeface.create(Typeface.SERIF, bold);
		} catch (Exception e) {
			typeface = Typeface.create(Typeface.DEFAULT, bold);
		}

		paint.setTypeface(typeface);
		paint.setTextSize(textSize * 2);
		paint.setLinearText(true);

		TextPaint textpaint = new TextPaint(paint);
		textpaint.setLinearText(true);
		android.text.StaticLayout staticLayout = new StaticLayout(printText, textpaint, printableArea, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
		int height = staticLayout.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(staticLayout.getWidth(), height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bitmap);
		c.drawColor(Color.WHITE);
		c.translate(0, 0);
		staticLayout.draw(c);

		StarBitmap starbitmap = new StarBitmap(bitmap, false, printableArea);

		command = starbitmap.getImageRasterDataForPrinting(true);

		return command;
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

			port.setEndCheckedBlockTimeoutMillis(30000);//Change the timeout time of endCheckedBlock method.
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
			alert.setCancelable(false);
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
