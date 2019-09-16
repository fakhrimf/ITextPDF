package com.pwpb.itextpdf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "PDFCreatorAct";
    private EditText edt;
    private Button btn;
    private File file;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private void initUI() {
        edt = findViewById(R.id.edt);
        btn = findViewById(R.id.btn);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions,REQUEST_CODE_ASK_PERMISSIONS);
    }

    private void initBtn() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString().isEmpty()) {
                    edt.setError("Isi text terlebih dahulu!");
                    edt.requestFocus();
                } else {
                    createPDF();
                    previewPdf();
                }
            }
        });
    }

    private void createPDF(){
        Document document = new Document();
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/Hello.pdf");
        Toast.makeText(this,"File di save di "+Environment.getExternalStorageDirectory().getPath() + "/Hello.pdf",Toast.LENGTH_SHORT).show();
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        Paragraph p = new Paragraph(edt.getText().toString());
        try {
            document.add(p);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

    private void previewPdf() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } else {
            Toast.makeText(this, "No PDF Viewer app found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initBtn();
    }
}
