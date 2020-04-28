package com.example.anhnen;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.anhnen.model.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ImageDetail extends AppCompatActivity {

    private ImageView imgDetail;
    private Photo photo;
    public ProgressDialog progressDialog;
    private Button btnSetWall;
    private Bitmap bitmap = null;
    String urlImg = null;
    private Button btnDownload;
    private long downloadID;
    private String[] P = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_REQUEST_CODE = 101;


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                if (getDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(ImageDetail.this, "Download Completed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageDetail.this, "Download not Completed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private int getDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            return status;
        }
        return DownloadManager.ERROR_UNKNOWN;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        btnSetWall = (Button) findViewById(R.id.btnSetWall);
        btnDownload = findViewById(R.id.btnDownload);
        imgDetail = (ImageView) findViewById(R.id.imgDetail);

        progressDialog = new ProgressDialog(ImageDetail.this);
        progressDialog.setMessage("Đang cài hình nền ...");
        progressDialog.setCancelable(false);

        Bundle b = getIntent().getExtras();
        photo = b.getParcelable("DATA");


        if (photo.getUrlS() != null) {
            urlImg = photo.getUrlS();
        } else if (photo.getUrlN() != null) {
            urlImg = photo.getUrlN();
        }else if (photo.getUrlL() != null){
            urlImg = photo.getUrlL();
        }

        Picasso.get()
                .load(urlImg).centerCrop().fit()
                .noFade()
                .into(imgDetail, new Callback() {
                    @Override
                    public void onSuccess() {
                        bitmap = ((BitmapDrawable) imgDetail.getDrawable()).getBitmap();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        btnSetWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    new SetWallpaperManager().execute(bitmap);
                } else {
                    Toast.makeText(ImageDetail.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }
        });

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        customDialog1().show();

                    } else {
                        requestPermission();
                    }
                } else {

                }


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }


    private void beginDownload(String url) {
/*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        DownloadManager.Request request = null;// Set if download is allowed on roaming network
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle("Image Download")// Title of the Download Notification
                    .setDescription("Downloading")// Description of the Download Notification
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                    .setDestinationInExternalFilesDir(ImageDetail.this, Environment.DIRECTORY_DOWNLOADS, "IMAGE.jpg")
                    .setRequiresCharging(false)// Set if charging is required to begin the download
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true);
        }
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

    }


    private class SetWallpaperManager extends AsyncTask<Bitmap, Integer, Void> {

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());
            for (int i = 0; i <= 99; i++) {
                SystemClock.sleep(10);
                publishProgress(i);
            }
            try {
                myWallpaperManager.setBitmap(bitmaps[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        Toast.makeText(ImageDetail.this, "Download fail", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }
            }, 10000);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(ImageDetail.this, "Cài hình nền thành công", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            moveTaskToBack(true);
        }
    }

    private boolean checkPermission() {
        for (String p : P) {
            int result = ContextCompat.checkSelfPermission(ImageDetail.this, p);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ImageDetail.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(ImageDetail.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ImageDetail.this, P, PERMISSION_REQUEST_CODE);
        }
    }

    private Dialog customDialog1() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogdownload);


        TextView text1;
        TextView text2;
        TextView text3;


        text1 = (TextView) dialog.findViewById(R.id.text1);
        text2 = (TextView) dialog.findViewById(R.id.text2);
        text3 = (TextView) dialog.findViewById(R.id.text3);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowload_img(0);
                dialog.cancel();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowload_img(1);
                dialog.cancel();
            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowload_img(2);
                dialog.cancel();
            }
        });


        return dialog;
    }


    private void dowload_img(int type) {

        String url = " ";

        if (type == 0) {
            url = photo.getUrlL();
        }
        if (type == 1) {
            url = photo.getUrlL();
        }

        if (type == 2) {
            url = photo.getUrlL();
        }
        Log.e("url", photo.getUrlS() + " " + photo.getUrlN() + " " + photo.getUrlL() + "");
        if (url != null) {
            beginDownload(url);
        } else {
            Toast.makeText(this, "Ảnh không có chất lượng này, vui lòng chọn kích cỡ khác", Toast.LENGTH_SHORT).show();
        }

    }
}
