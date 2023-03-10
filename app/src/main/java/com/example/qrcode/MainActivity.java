package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //View Object
    private Button buttonScanning;
    private TextView textViewName, textViewClass, textViewId;

    //qrcode scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //View Object
        buttonScanning = (Button) findViewById(R.id.buttonscan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewClass = (TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView) findViewById(R.id.textViewNim);

        //inisialisasi scan object
        qrScan = new IntentIntegrator(this);

        //implementasi oneclick listener
        buttonScanning.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //jika hasil scanner tidak sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil Scan tidak ada", Toast.LENGTH_LONG).show();
            }
            //webview
            else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            }
            else {
                //Jika qrcode ada isinya
                try {
                    //Konversi data ke json
                    JSONObject obj = new JSONObject(result.getContents());
                    //diset nilai datanya ke textview
                    textViewName.setText(obj.getString("nama"));
                    textViewClass.setText(obj.getString("Kelas"));
                    textViewId.setText(obj.getString("NIM"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
                //DIAL UP, NOMOR TELEPON
                try {
                    Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse(result.getContents()));
                    startActivity(intent2);
                }
                catch (Exception e) {
                    Toast.makeText(this, "Not Scanned", Toast.LENGTH_LONG).show();


                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();

    }
}
