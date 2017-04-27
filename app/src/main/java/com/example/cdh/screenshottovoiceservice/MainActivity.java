package com.example.cdh.screenshottovoiceservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.Locale;

import static java.util.logging.Logger.global;

public class MainActivity extends AppCompatActivity {
    public TextToSpeech speaker;
    public RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restClient = new RestClient();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.INTERNET},
                    1);
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if(status != TextToSpeech.ERROR)
                {
                    speaker.setLanguage(Locale.US);
                    if(Build.VERSION.RELEASE.startsWith("5"))
                        speaker.speak("Application Started", TextToSpeech.QUEUE_FLUSH, null, null);
                    else
                        speaker.speak("Application Started", TextToSpeech.QUEUE_FLUSH, null,null);

                }
            }
        });

        Intent serviceIntent = new Intent(this, FileObserverService.class);
        startService(serviceIntent);
    }

    public void TestRestAPI(View view)
    {
        Log.d("Test Get", "Sending Get Request");
        RequestParams rp = new RequestParams();
        rp.add("username", "cdheath"); rp.add("password", "");
        //speaker.speak(restClient.getTestFromRestApi(rp),TextToSpeech.QUEUE_FLUSH, null,null);
    }

    public void SpeakString(String str)
    {
        speaker.speak(str,TextToSpeech.QUEUE_FLUSH, null,null);
    }

}
