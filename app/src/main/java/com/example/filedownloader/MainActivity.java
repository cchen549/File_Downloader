package com.example.filedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button bt_start;
    private TextView process;
    private volatile boolean stopThread = false;

    class ExampleRunnable implements Runnable{
        @Override
        public void run(){
            mockFileDownloader();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_start = findViewById(R.id.bt_start);
        process = (TextView) findViewById(R.id.process);

    }

    public void mockFileDownloader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_start.setText("DOWNLOADING...");
            }
        });


        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress+10){
            if(stopThread){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bt_start.setText("Start");
                        process.setText("");
                    }
                });
                return;
            }
             int prog = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    process.setText("Download Progress: " + prog + "%");
                }
            });
            Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_start.setText("Start");
                process.setText("");
            }
        });
    }

    public void startDownload(View view){
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }
}