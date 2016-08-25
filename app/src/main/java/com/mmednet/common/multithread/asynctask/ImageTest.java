package com.mmednet.common.multithread.asynctask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mmednet.common.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;

public class ImageTest extends AppCompatActivity {

    private static final String TAG = ImageTest.class.getSimpleName();

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Button mBtnLoadImage;
    private static final int FLAG_REQUEST_INTERNET = 0x001;

    private static final String URI = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=177271374,3380489282&fm=80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_image_progress);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mProgressBar = (ProgressBar) findViewById(R.id.progreebar);
        mBtnLoadImage = (Button) findViewById(R.id.btn_loadimage);

        mBtnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ImageTest.this,
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageTest.this,
                            new String[]{Manifest.permission.INTERNET},
                            FLAG_REQUEST_INTERNET);
                } else {
                    new MyAsyncTask().execute(URI);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == FLAG_REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new MyAsyncTask().execute(URI);
            } else {
                Toast.makeText(ImageTest.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Params 启动任务执行的输入参数，比如HTTP请求的URL。
     * Progress 后台任务执行的百分比。
     * Result 后台执行任务最终返回的结果，比如Strin
     */
    public class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            URLConnection connection = null;
            InputStream is = null;
            BufferedInputStream bis = null;
            Bitmap bitmap = null;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                bis = new BufferedInputStream(is);
                bitmap = BitmapFactory.decodeStream(bis);
                is.close();
                bis.close();
            } catch (IOException e) {
                Log.d(TAG, "doInBackground:IOException:" + Log.getStackTraceString(e));
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mProgressBar.setVisibility(View.GONE);
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
