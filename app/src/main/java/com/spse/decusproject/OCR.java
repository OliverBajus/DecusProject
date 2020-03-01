package com.spse.decusproject;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class OCR extends AppCompatActivity {

    SurfaceView camera;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String result = "nic";
    SparseArray<TextBlock> items;
    Dialog dialog;
    TextView txtResult;
    Button ano, nie;

    private static final int PERMISSION_REQUEST_CODE = 1;

    TextRecognizer textRecognizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_up);
        camera = findViewById(R.id.surfaceView);
        textView = findViewById(R.id.text_view);

        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("OCR", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            camera.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OCR.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(camera.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
            startRecognizing();
        }

    }

    private void startRecognizing() {
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {

            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                SparseArray<TextBlock> items = detections.getDetectedItems();
                if (!dialog.isShowing() && items.size() > 0) {

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    textView.setText(stringBuilder);
                    final String ingrediencie = validateResult(items);
                    Log.d("vysledok", "vysledok: " + ingrediencie);
                    if (result != null) {
                        textView.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showPopUp(result);
                                    }
                                }
                        );
                    }

                }
            }
        });
    }

    private void stopRecognizing() {
        textRecognizer.release();
    }

    public void showPopUp(final String ingrediencie) {
        txtResult = dialog.findViewById(R.id.txtResultDrawable);
        ano = dialog.findViewById(R.id.anoButton);
        nie = dialog.findViewById(R.id.nieButton);

        txtResult.setText(ingrediencie);

        ano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OCR.this, HomeFragment.class);
                intent.putExtra("ingrediencie",ingrediencie);
                startActivity(intent);
            }
        });

        nie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public String validateResult(SparseArray<TextBlock> items) {
        for (int i = 0; i < items.size(); ++i) {
            String[] itemValues = items.valueAt(i).getValue().trim().split("\\s+");
            for (String item : itemValues) {
                Log.d("item", "item: " + item);
                if (Pattern.matches("[0-9]{3}", item)) {
                    return item;
                }
            }
        }
        return null;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(OCR.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(OCR.this, Manifest.permission.CAMERA)) {
            Toast.makeText(OCR.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(OCR.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        stopRecognizing();
        super.onDestroy();
    }
}
