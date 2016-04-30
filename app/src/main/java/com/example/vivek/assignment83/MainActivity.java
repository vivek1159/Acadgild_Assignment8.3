package com.example.vivek.assignment83;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    String TAG = "Download:";
    Button DownloadButton;
    public ProgressBar ProgressBar1, ProgressBar2, ProgressBar3;
    ImageView ImageView1, ImageView2, ImageView3;

    String urls[] = {"http://img6a.flixcart.com/image/mobile/g/2/z/asus-zenfone-2-laser-ze600kl-400x400-imae9tftbfdcxzng.jpeg",
            "http://img6a.flixcart.com/image/laptop-bag/q/f/j/polyester-lenovo-laptop-backpack-b3055-400x400-imaeacxdaw3jy2zw.jpeg",
            "http://img5a.flixcart.com/image/computer/y/g/g/hp-notebook-400x400-imaebukfk7hghnuw.jpeg"};

    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DownloadButton = (Button) findViewById(R.id.DownloadButton);

        ProgressBar1 = (ProgressBar) findViewById(R.id.ProgressBar1);
        ProgressBar2 = (ProgressBar) findViewById(R.id.ProgressBar2);
        ProgressBar3 = (ProgressBar) findViewById(R.id.ProgressBar3);

        ImageView1 = (ImageView) findViewById(R.id.ImageView1);
        ImageView2 = (ImageView) findViewById(R.id.ImageView2);
        ImageView3 = (ImageView) findViewById(R.id.ImageView3);
    }


    public void onClickDownloadButton(View v) {
        Log.d(TAG,"Download clicked");
        DownloadImage image1 = new DownloadImage();
        image1.where = "image1.jpg";
        image1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,urls[0]);

        DownloadImage image2 = new DownloadImage();
        image2.where = "image2.jpg";
        image2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urls[1]);

        DownloadImage image3 = new DownloadImage();
        image3.where = "image3.jpg";
        image3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urls[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class DownloadImage extends AsyncTask<String, Integer, String> {

        String where = "";

        ContextWrapper contextWrapper;

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"in Background");
            contextWrapper = new ContextWrapper(getApplicationContext());
            int count = 0, total = 0;

            try {
                URL url = new URL(params[0]);

                Log.d(TAG,"opening connection");
                URLConnection connection = url.openConnection();
                connection.connect();
                Log.d(TAG, "Conn. opened");
                int length = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(),8192);
                Log.d(TAG,"Input initialized");
                OutputStream output = new FileOutputStream(contextWrapper.getFilesDir() + "/" + where);
                Log.d(TAG,"Output initialized");
                byte data[] = new byte[2048];

                while ((count = input.read(data)) != -1) {
                    Log.d(TAG,"Count " +count + " total " + total);
                    total += count;
                    Log.d(TAG,"calling publish progress");
                    publishProgress((total * 100) / length);
                    output.write(data, 0, count);
                }

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG,"Progress update");
            switch (where) {
                case "image1.jpg":
                    ProgressBar1.setProgress(values[0]);
                    break;
                case "image2.jpg":
                    ProgressBar2.setProgress(values[0]);
                    break;
                case "image3.jpg":
                    ProgressBar3.setProgress(values[0]);
                    break;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String Image_Path = contextWrapper.getFilesDir() + "/" + where;
            Log.d(TAG,"Storing in " + Image_Path);
            switch (where) {
                case "image1.jpg":
                    ImageView1.setImageDrawable(Drawable.createFromPath(Image_Path));
                    ProgressBar1.setVisibility(View.GONE);
                    ImageView1.setVisibility(View.VISIBLE);
                    break;
                case "image2.jpg":
                    ImageView2.setImageDrawable(Drawable.createFromPath(Image_Path));
                    ProgressBar2.setVisibility(View.GONE);
                    ImageView2.setVisibility(View.VISIBLE);
                    break;
                case "image3.jpg":
                    ImageView3.setImageDrawable(Drawable.createFromPath(Image_Path));
                    ProgressBar3.setVisibility(View.GONE);
                    ImageView3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}