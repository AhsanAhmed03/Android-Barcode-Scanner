package com.scanbot.sdk.android.barcode.scanner;

import android.app.Application;

import io.scanbot.sdk.barcode.ScanbotBarcodeDetector;
import io.scanbot.sdk.barcode_scanner.ScanbotBarcodeScannerSDKInitializer;

public class MyApplication extends Application {

    private final static String LICENSE_KEY = "izeCMhUb1VNO4k8NRQP9KXccKwIQQu" +
            "7t4Xce4XoJ6yl5aeBuhXioQe6aLK6H" +
            "/Ky+Rn9u/itOQmKIbMLPCckRbxvXk5" +
            "PAOZr8QWq4XlTVTY/J7zX0DjbVODkL" +
            "aIrUi6m0NMmn0E2l6er+gyofSx2nsY" +
            "xnrsPlpH/jJmO+teKigBc9/iUDZp6z" +
            "SjP4NpqMPXVIXfCZ7fxakYCuj6Z8jY" +
            "UrkjHVWAcNH4DaF5DKX13RncOesWwr" +
            "1Y1SwNaQg9tOGIK5rKbXe6/aAiZdX1" +
            "WFjgxCvyueHQiMm+f95hAR7v97T3ut" +
            "BAYA1VfWMgUYQ7UT2UV4l9TJNwJdPo" +
            "xSWgU65yFGuQ==\nU2NhbmJvdFNESw" +
            "pjb20uc2NhbmJvdC5zZGsuYW5kcm9p" +
            "ZC5iYXJjb2RlLnNjYW5uZXIKMTY5ND" +
            "kwODc5OQo4Mzg4NjA3CjE5\n";

    @Override
    public void onCreate() {
        super.onCreate();
        new ScanbotBarcodeScannerSDKInitializer().license(this,LICENSE_KEY)
                .initialize(this);
    }
}
