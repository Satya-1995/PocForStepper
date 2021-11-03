package com.example.pocforstepper.sdk29;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocforstepper.R;

import java.io.File;
import java.io.IOException;

public class ImageTest extends AppCompatActivity {

    private static final String TAG = "ImageTest";


    private Button btnFetchImage;
    private TextView tvSourcePath, tvDestinationPath, tvStatus;
    File sourcePath, destinationPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);

//        ivThumb = findViewById(R.id.iv_thumb);
//        edImageName = findViewById(R.id.ed_image_name);
        btnFetchImage = findViewById(R.id.btn_fetch_image);
        tvSourcePath = findViewById(R.id.tv_source_path);
        tvStatus = findViewById(R.id.tv_status);
        tvDestinationPath = findViewById(R.id.tv_destination_path);

        sourcePath = new File(Environment.getExternalStoragePublicDirectory("Cropin"), "farmer/");
        destinationPath = ImageTest.this.getExternalFilesDir("");
        tvSourcePath.setText(sourcePath.getAbsolutePath());
        tvDestinationPath.setText(destinationPath.getAbsolutePath());
        FileUtil.FileCopyListener mListener = fileName -> {
            Log.d("TAG", "The name is " + fileName);
            Toast.makeText(this,fileName,Toast.LENGTH_SHORT).show();
        };

        btnFetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //FileUtil.copyDirectoryOneLocationToAnotherLocation(sourcePath, destinationPath, mListener);
                        FileUtil.copyFileOrDirectory(sourcePath.getAbsolutePath(), destinationPath.getAbsolutePath(),mListener);
                    }
                });

            }
        });

    }
}