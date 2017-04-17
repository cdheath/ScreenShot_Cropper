package com.example.cdh.screenshottovoiceservice;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class FileObserverService extends Service {
      //public final String path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "Screenshots" + File.separator;
     //public final String path = "Internal storage device" +File.separator + Environment.DIRECTORY_PICTURES +File.separator +"Screenshots" +File.separator;
     //public final String path = File.separator +"storage" +File.separator +"emulated" +File.separator +"0" +File.separator +"Pictures" +File.separator +"Screenshots" +File.separator;
    //public final String path = "Phone" +File.separator + Environment.DIRECTORY_PICTURES +File.separator +"Screenshots" +File.separator;
     // public final String path = Environment.getDataDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "Screenshots" + File.separator;
    public final String path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "Screenshots" + File.separator;

    public static File fileToObserve = new File(Environment.getExternalStorageDirectory(), "Pictures" + File.separator + "Screenshots");
    public static FileObserver observer;
    public static TextToSpeech speaker;

    private static final int START_X = 0;
    private static final int START_Y = 0;
    private static final int WIDTH_PX = 1440;
    private static final int HEIGHT_PX = 1530;
    public static final int CREATE_DIR = 0x40000100;
    public FileObserverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId)
    {

        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if(status != TextToSpeech.ERROR)
                {
                    speaker.setLanguage(Locale.US);
                    if(Build.VERSION.RELEASE.startsWith("5"))
                        speaker.speak("Service Initializing " + path, TextToSpeech.QUEUE_FLUSH, null, null);
                    else
                        speaker.speak("Service Initializing - Monitoring Path " +fileToObserve.getAbsolutePath(), TextToSpeech.QUEUE_FLUSH, null);


                }
            }
        });

        try {
            observer = new FileObserver(fileToObserve.getCanonicalPath(), FileObserver.ALL_EVENTS) {
                @Override
                public void onEvent(int event, String file) {
                    Log.e("File Received", "Found File " +file +" " +event);

                    File imgFile = new File(fileToObserve.getAbsolutePath()+File.separator +file);
                    speaker.speak("File Received " +imgFile.getName(), TextToSpeech.QUEUE_FLUSH, null);
                    if (imgFile.exists()) {
                        if( event == 16) {
                            Bitmap newBitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(fileToObserve.getAbsolutePath() + File.separator + file), START_X, START_Y, WIDTH_PX, HEIGHT_PX, null, false);
                            imgFile.delete();
                            //File newFile = new File(fileToObserve.getAbsolutePath() +File.separator+ imgFile.getName() + "_trim.png");

                            FileOutputStream out = null;
                            try {
                                if(!new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + imgFile.getName() + "_trim.png").exists()) {
                                    Log.d("Create New Image File", "Starting Image File Parse and Saving");
                                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + imgFile.getName() + "_trim.png");
                                    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

                                    // PNG is a lossless format, the compression factor (100) is ignored
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Create New Image File", "Error During Image File Parse and Saving");
                            } finally {
                                try {
                                    if (out != null) {
                                        Log.d("Create New Image File", "Closing FileStream");
                                        out.close();
                                        if (new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + imgFile.getName() + "_trim.png").exists()) {
                                            Log.d("Create New Image File", "New file found adding to media");
                                            MediaStore.Images.Media.insertImage(getContentResolver(), Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + imgFile.getName() + "_trim.png", imgFile.getName() + "_trim.png", imgFile.getName() + "_trim.png");
                                            speaker.speak("Trimmed file created" + imgFile.getName() + "_trim.png", TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                } catch (IOException e) {
                                    Log.e("Create New Image File", "Error During Stream closing or adding to media");
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    else
                    {
                        Log.e("Create New Image File","Source File Does Not Exist");
                    }
                }
            };
        }
        catch (IOException e) {
            speaker.speak("Failed to Find Directory", TextToSpeech.QUEUE_FLUSH, null);
            e.printStackTrace();
        }

        observer.startWatching();

        return START_NOT_STICKY;
    }
}
