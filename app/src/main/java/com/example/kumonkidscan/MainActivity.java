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

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    RequestQueue queue;

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
    public  void volleyrequest(String studentName){
        queue = Volley.newRequestQueue(this);
        String url = "https://scannerdatabase.sites.tjhsst.edu/hbs?name=";
        url+= studentName;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                try {
//                    //format response to JSON object called "data"
//                    JSONObject object = new JSONObject("{\"data\":"+response+"}");
//                    //conver object to an array of objects
//                    JSONArray array = object.getJSONArray("data");
//                    //get first object and display its id
//                    textView.setText("id: " + array.getJSONObject(0).getString("id"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley error"+ error.toString());
            }
        });
        queue.add(request);

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
            package_to_be_sent = result.getContents();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Sent to server");
            volleyrequest(package_to_be_sent);
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