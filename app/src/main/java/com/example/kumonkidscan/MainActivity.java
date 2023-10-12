package com.example.kumonkidscan;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    String package_to_be_sent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
           scanCode();

        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        qrLauncher.launch(options);
    }
    //id types:
    // 1 = qr code sent Id
    ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(new ScanContract(), result -> {
       if(result.getContents() != null){
            package_to_be_sent = "1 " + result.getContents();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Sent to server");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}