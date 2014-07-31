package com.StarMicronics.StarIOSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.StarMicronics.StarIOSDK.PrinterFunctions.CutType;

public class cutActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cut);
    }
	
	public void FullCut(View view)
	{
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();
		
		PrinterFunctions.performCut(this, portName, portSettings, CutType.FULL_CUT);
	}
	
	public void PartialCut(View view)
	{
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();
		
		PrinterFunctions.performCut(this, portName, portSettings, CutType.PARTIAL_CUT);
	}
	
	public void FullCutWithFeed(View view)
	{
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();
		
		PrinterFunctions.performCut(this, portName, portSettings, CutType.FULL_CUT_FEED);
	}
	
	public void PartialCutWithFeed(View view)
	{
		if (!checkClick.isClickEvent()) return;
		String portName = PrinterTypeActivity.getPortName();
		String portSettings = PrinterTypeActivity.getPortSettings();
		
		PrinterFunctions.performCut(this, portName, portSettings, CutType.PARTIAL_CUT_FEED);
	}
	
	public void Help(View view) 
	{
		if (!checkClick.isClickEvent()) return;
		String helpString = "<body><UnderlineTitle>CUT</UnderlineTitle><br/><br/>" +
                			"<Code>ASCII:</code> <CodeDef>ESC d <StandardItalic>n</StandardItalic></CodeDef><br/>" +
                			"<Code>Hex:</Code> <CodeDef>1B 54 <StandardItalic>n</StandardItalic></CodeDef><br/><br/>" +
                			"<div class=\"div-tableCut\">" +
                				"<div class=\"div-table-rowCut\">" +
                				"<div class=\"div-table-colFirstCut\"></div>" +
                				"<div class=\"div-table-colCut\"><It1>n =</It1></div>" +
                				"<div class=\"div-table-colCut2\"><It1>0,1,2, or 3</It1></div>" +
                			"</div>" + 
                			"<div class=\"div-table-rowCut\">" +
                				"<div class=\"div-table-colFirstCut\"></div>" +
                				"<div class=\"div-table-colCut\"><It1>0 =</It1></div>" +
                				"<div class=\"div-table-colCut2\"><It1>Full cut at current position</It1></div>" +
                			"</div>" +
                			"<div class=\"div-table-rowCut\">" +
                				"<div class=\"div-table-colFirstCut\"></div>" +
                				"<div class=\"div-table-colCut\"><It1>1 =</It1></div>" +
                				"<div class=\"div-table-colCut2\"><It1>Partial cut at current position</It1></div>" +
                			"</div>" +
                			"<div class=\"div-table-rowCut\">" +
                				"<div class=\"div-table-colFirstCut\"></div>" +
                				"<div class=\"div-table-colCut\"><It1>2 =</It1></div>" +
                				"<div class=\"div-table-colCut2\"><It1>Paper is fed to cutting position, then full cut</It1></div>" +
                			"</div>" +
                			"<div class=\"div-table-rowCut\">" +
                				"<div class=\"div-table-colFirstCut\"></div>" +
                				"<div class=\"div-table-colCut\"><It1>3 =</It1></div>" +
                				"<div class=\"div-table-colCut2\"><It1>Paper is fed to cutting position, then partial cut</It1></div>" +
                			"</div>" +
                			"</div>";
		helpMessage.SetMessage(helpString);
			 
		Intent myIntent = new Intent(this, helpMessage.class);
		startActivityFromChild(this, myIntent, 0);
	 }
}
