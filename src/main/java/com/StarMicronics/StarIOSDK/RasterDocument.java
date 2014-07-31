package com.StarMicronics.StarIOSDK;

public class RasterDocument {
	// Represaent a star raster mode page

	public enum RasTopMargin {Default, Small, Standard}

	;

	public enum RasSpeed {Full, Medium, Low}

	;

	public enum RasPageEndMode {Default, None, FeedToCutter, FeedToTearbar, FullCut, FeedAndFullCut, PartialCut, FeedAndPartialCut, Eject, FeedAndEject}

	;

	RasTopMargin mTopMargin = RasTopMargin.Default;
	RasSpeed mSpeed = RasSpeed.Full;
	RasPageEndMode mFFMode = RasPageEndMode.Default;
	RasPageEndMode mEOTMode = RasPageEndMode.Default;
	int mPageLength = 0;
	int mLeftMargin = 0;
	int mRightMargin = 0;

	public RasterDocument() {
	}

	public RasterDocument(RasSpeed speed, RasPageEndMode endOfPageBehaviour, RasPageEndMode endOfDocumentBahaviour, RasTopMargin topMargin, int pageLength, int leftMargin, int rightMargin) {
		mSpeed = speed;
		mEOTMode = endOfDocumentBahaviour;
		mFFMode = endOfPageBehaviour;
		mTopMargin = topMargin;
		mPageLength = pageLength;
		mLeftMargin = leftMargin;
		mRightMargin = rightMargin;
	}

	public RasTopMargin getTopMargin() {
		return mTopMargin;
	}

	public void setTopMargin(RasTopMargin value) {
		mTopMargin = value;
	}

	public RasSpeed getPrintSpeed() {
		return mSpeed;
	}

	public void setPrintSpeed(RasSpeed value) {
		mSpeed = value;
	}

	public RasPageEndMode getEndOfPageBehaviour() {
		return mFFMode;
	}

	public void setEndOfPageBehaviour(RasPageEndMode value) {
		mFFMode = value;
	}

	public RasPageEndMode getEndOfDocumentBehaviour() {
		return mEOTMode;
	}

	public void setEndOfDocumentBehaviour(RasPageEndMode value) {
		mEOTMode = value;
	}

	public int getLeftMargin() {
		return mLeftMargin;
	}

	public void setLeftMargin(int value) {
		mLeftMargin = value;
	}

	public int getRightMargin() {
		return mRightMargin;
	}

	public void setRightMargin(int value) {
		mRightMargin = value;
	}

	public int getPageLength() {
		return mPageLength;
	}

	public void setPageLength(int value) {
		mPageLength = value;
	}

	public byte[] BeginDocumentCommandData() {
		String command = "\u001b*rA";
		command += TopMarginCommand(mTopMargin);
		command += SpeedCommand(getPrintSpeed());
		command += PageLengthCommand(mPageLength);
		command += LeftMarginCommand(mLeftMargin);
		command += RightMarginCommand(mRightMargin);
		command += SetEndOfPageModeCommand(getEndOfDocumentBehaviour());
		command += SetEndOfDocModeCommand(getEndOfDocumentBehaviour());
		return command.getBytes();
	}

	public byte[] EndDocumentCommandData() {
		return "\u001b*rB".getBytes();
	}

	public byte[] PageBreakCommandData() {
		return "\u001b\u000c\0".getBytes();
	}

	private String TopMarginCommand(RasTopMargin source) {
		final String cmd = "\u001b*rT";

		switch (source) {
			case Default:
				return cmd + "0\0";
			case Small:
				return cmd + "1\0";
			case Standard:
				return cmd + "2\0";
		}

		return cmd + "0\0";
	}

	private String SpeedCommand(RasSpeed source) {
		final String cmd = "\u001b*rQ";

		switch (source) {
			case Full:
				return cmd + "0\0";
			case Medium:
				return cmd + "1\0";
			case Low:
				return cmd + "2\0";
		}

		return cmd + "0\0";
	}

	private String LeftMarginCommand(int margin) {
		return "\u001b*rml" + Integer.toString(margin).trim() + "\0";
	}

	private String RightMarginCommand(int margin) {
		return "\u001b*rmr" + Integer.toString(margin).trim() + "\0";
	}

	private String PageLengthCommand(int pLength) {
		return "\u001b*rP" + Integer.toString(pLength).trim() + "\0";
	}

	private String EndPageModeCommandValue(RasPageEndMode mode) {
		switch (mode) {
			case Default:
				return "0";
			case None:
				return "1";
			case FeedToCutter:
				return "2";
			case FeedToTearbar:
				return "3";
			case FullCut:
				return "8";
			case FeedAndFullCut:
				return "9";
			case PartialCut:
				return "12";
			case FeedAndPartialCut:
				return "13";
			case Eject:
				return "36";
			case FeedAndEject:
				return "37";
			default:
				return "0";
		}
	}

	private String SetEndOfPageModeCommand(RasPageEndMode mode) {
		return "\u001b*rF" + EndPageModeCommandValue(mode) + "\0";
	}

	private String SetEndOfDocModeCommand(RasPageEndMode mode) {
		return "\u001b*rE" + EndPageModeCommandValue(mode) + "\0";
	}

}

