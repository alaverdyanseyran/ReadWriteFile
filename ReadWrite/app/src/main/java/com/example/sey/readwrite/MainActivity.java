package com.example.sey.readwrite;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
private EditText editText;
private String fileName, data ;
RadioGroup radioGroup; RadioButton rint, rext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.et);
        final Button save = findViewById(R.id.save);
        final TextView textView = findViewById(R.id.tv);
        final Button load = findViewById(R.id.load);
        radioGroup = findViewById(R.id.rg);
        rint =findViewById(R.id.intern);
        rext =findViewById(R.id.extern);
        fileName = "infile";
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = (editText.getText().toString());
                if (radioGroup.getCheckedRadioButtonId() == R.id.extern)
                { int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    saveDataInExtFile(fileName, data);
                }
            }
         else {saveDataInFile(fileName, data);}}

        });

        load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.extern){
                try {
                    textView.setText(loadExtDataFile(fileName));
                } catch (IOException e) {
                    Log.d("===", "IOEX");
                    e.printStackTrace();
                }
            } else {
                    try {
                        textView.setText(loadDataFile(MainActivity.this, fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }}
        });
    }

    private void saveDataInFile(String fileName, String data) {

        try {
            FileOutputStream fout = openFileOutput(fileName, Context.MODE_PRIVATE);
            byte buffer[] = data.getBytes();
            fout.write(buffer);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
           Log.d("===", "save");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String loadDataFile(Context context, String fileName) throws IOException {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            Log.d("===", "filenotfound");
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line="";
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
    private void saveDataInExtFile(String fileName, String data) {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File file = new File(dir, fileName);
        try {


            FileWriter fout = new FileWriter(file);
            //byte buffer[] = data.getBytes();
            fout.write(data);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            Log.d("===", file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private String loadExtDataFile(String fileName) throws IOException {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File file = new File(dir, fileName);
        FileReader fr=null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            Log.d("===", "filenotfound");
            e.printStackTrace();
        }


        BufferedReader bufferedReader = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        String line="";
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
Log.d("===",sb.toString());
        return sb.toString();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
           {int grantResultsLength = grantResults.length;
            if(grantResultsLength > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                saveDataInExtFile(fileName,editText.getText().toString());}

    }
}
