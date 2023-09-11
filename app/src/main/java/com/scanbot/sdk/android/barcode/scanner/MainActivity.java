package com.scanbot.sdk.android.barcode.scanner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.MessageFormat;
import java.util.ArrayList;

import io.scanbot.sdk.AspectRatio;
import io.scanbot.sdk.SdkLicenseError;
import io.scanbot.sdk.barcode.BarcodeDetectorFrameHandler;
import io.scanbot.sdk.barcode.ScanbotBarcodeDetector;
import io.scanbot.sdk.barcode.entity.BarcodeScanningResult;
import io.scanbot.sdk.barcode_scanner.ScanbotBarcodeScannerSDK;
import io.scanbot.sdk.camera.CameraOpenCallback;
import io.scanbot.sdk.camera.FrameHandlerResult;
import io.scanbot.sdk.ui.camera.FinderOverlayView;
import io.scanbot.sdk.ui.camera.ScanbotCameraXView;

public class MainActivity extends AppCompatActivity {

    ScanbotCameraXView cameraXview;
    BarcodeDetectorFrameHandler barcodeDetectorFrameHandler;
    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraXview = findViewById(R.id.camera);
        cameraXview.setCameraOpenCallback(new CameraOpenCallback() {
            @Override
            public void onCameraOpened() {
                cameraXview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cameraXview.continuousFocus();
                    }
                },500);
            }
        });
        ScanbotBarcodeDetector scanbotBarcodeDetector = new ScanbotBarcodeScannerSDK(this).createBarcodeDetector();
        barcodeDetectorFrameHandler = BarcodeDetectorFrameHandler.attach(cameraXview,scanbotBarcodeDetector);
        barcodeDetectorFrameHandler.setDetectionInterval(100);
        barcodeDetectorFrameHandler.addResultHandler(new BarcodeDetectorFrameHandler.ResultHandler() {
            @Override
            public boolean handle(@NonNull FrameHandlerResult<? extends BarcodeScanningResult, ? extends SdkLicenseError> frameHandlerResult) {
                BarcodeScanningResult result = ((FrameHandlerResult.Success<BarcodeScanningResult>) frameHandlerResult).getValue();
                if (result != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.result_bottom_sheet,null,false);
                            bottomSheetDialog.setContentView(view);
//                            bottomSheetDialog.show();

                            TextView format =view.findViewById(R.id.format_txt);
                            TextView resultTxt =view.findViewById(R.id.result_txt);
                            ImageView imageView =view.findViewById(R.id.imageView);

                            format.setText(MessageFormat.format("Format: {0}",result.getBarcodeItems()
                                    .get(0).getBarcodeFormat().name()));
                            resultTxt.setText(MessageFormat.format("Result: {0}",result.getBarcodeItems().get(0).getText()));

                            imageView.setImageBitmap(result.getBarcodeItems().get(0).getImage());

                            bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    barcodeDetectorFrameHandler.setEnabled(true);
                                }
                            });
                        }
                    });
                }
                return false;
            }
        });

        FinderOverlayView finderOverlayView = findViewById(R.id.finder);
        ArrayList<AspectRatio> aspectRatios = new ArrayList<>();
        aspectRatios.add(new AspectRatio(200,200));
        finderOverlayView.setRequiredAspectRatios(aspectRatios);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraXview.stopPreview();
    }
}