StarIoAndroid
=============

Compare to project that you found here: http://www.starasia.com/tsp650driverdownload.asp

The benefits of this project you coulud have are:

* Gradle support for demo app, so you could Run `gradle assemble` and build it from AndroidStudio or Intellij

* Standardize source directory hierarchy

~~~~java
├── assets
├── build.gradle
├── local.properties
├── proguard.cfg
├── project.properties
├── README.md
├── src
│   └── main
│       ├── AndroidManifest.xml
│       ├── java
│       │   └── com
│       │       └── StarMicronics
│       │           └── StarIOSDK
│       │               ├── BarcodePrintingMini.java
│       │               ├── barcodeselector2d.java
│       │               ├── barcodeselector.java
│       │               ├── checkClick.java
│       │               ├── code128Activity.java
│       │               ├── code39Activity.java
│       │               ├── code93Activity.java
│       │               ├── CommandTypeActivity.java
│       │               ├── cutActivity.java
│       │               ├── DKAirCashActivity.java
│       │               ├── DKAirCashActivity.java.bak
│       │               ├── helpActivity.java
│       │               ├── helpMessage.java
│       │               ├── imagePrintingActivity.java
│       │               ├── ITFActivity.java
│       │               ├── kanjiTextFormatingActivity.java
│       │               ├── kanjiTextFormatingMiniActivity.java
│       │               ├── LineModeHelpActivity.java
│       │               ├── MiniPrinterFunctions.java
│       │               ├── pdf417Activity.java
│       │               ├── pdf417miniActivity.java
│       │               ├── PrinterFunctions.java
│       │               ├── PrinterTypeActivity.java
│       │               ├── QRCodeActivity.java
│       │               ├── QrcodeMiniActivity.java
│       │               ├── RasterDocument.java
│       │               ├── RasterModeHelpActivity.java
│       │               ├── rasterPrintingActivity.java
│       │               ├── StarBitmap.java
│       │               ├── StarIOSDKMobilePrinterActivity.java
│       │               ├── StarIOSDKPOSPrinterLineModeActivity.java
│       │               ├── StarIOSDKPOSPrinterRasterModeActivity.java
│       │               ├── textFormatingActivity.java
│       │               └── textFormatingMiniActivity.java
│       └── res
│           ├── drawable-hdpi
│           │   ├── code128.gif
│           │   ├── code39.gif
│           │   ├── code93.gif
│           │   ├── icon.png
│           │   ├── itf.gif
│           │   ├── pdf417.gif
│           │   ├── posprinter.jpg
│           │   ├── printer.jpg
│           │   └── qrcode.gif
│           ├── drawable-ldpi
│           │   ├── code128.gif
│           │   └── qrcode.gif
│           ├── drawable-mdpi
│           │   ├── code128.gif
│           │   ├── code39.gif
│           │   ├── code93.gif
│           │   ├── icon.png
│           │   ├── itf.gif
│           │   ├── pdf417.gif
│           │   ├── posprinter.jpg
│           │   ├── printer.jpg
│           │   └── qrcode.gif
│           ├── drawable-nodpi
│           │   ├── icon.png
│           │   ├── image1.png
│           │   ├── image2.jpg
│           │   ├── image3.bmp
│           │   ├── image4.gif
│           │   ├── posprinter.jpg
│           │   ├── printer.jpg
│           │   └── staricon.png
│           ├── drawable-tvdpi
│           │   ├── code128.gif
│           │   ├── code39.gif
│           │   ├── code93.gif
│           │   ├── icon.png
│           │   ├── itf.gif
│           │   ├── pdf417.gif
│           │   ├── posprinter.jpg
│           │   ├── printer.jpg
│           │   └── qrcode.gif
│           ├── layout
│           │   ├── barcodeselector.xml
│           │   ├── barcode.xml
│           │   ├── commandtype.xml
│           │   ├── cut.xml
│           │   ├── dkaircash.xml
│           │   ├── helpmessage.xml
│           │   ├── help.xml
│           │   ├── kanjitextformating.xml
│           │   ├── main.xml
│           │   ├── mini_barcode.xml
│           │   ├── mini_kanjitextformating.xml
│           │   ├── mini_pdf417code.xml
│           │   ├── mini_qrcode.xml
│           │   ├── mini_textformating.xml
│           │   ├── pft417code.xml
│           │   ├── printertype.xml
│           │   ├── printingimage.xml
│           │   ├── printingtextasimage.xml
│           │   ├── qrcode.xml
│           │   └── textformating.xml
│           ├── values
│           │   └── strings.xml
│           ├── values-ja
│           │   └── strings.xml
│           └── xml
│               └── device_filter.xml
└── StarIO Android SDK.iml
~~~~
