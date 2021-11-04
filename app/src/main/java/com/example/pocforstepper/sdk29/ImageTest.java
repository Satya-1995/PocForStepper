package com.example.pocforstepper.sdk29;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocforstepper.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

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


        btnFetchImage.setOnClickListener(v -> checkForPermission());
    }

    private void checkForPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Handler handler = new Handler();
                handler.post(() -> {
                    copyFiles();
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                if (permissionDeniedResponse.isPermanentlyDenied()) {
                    showSettingsDialog();
                } else {
                    showPermissionRequiredDialogue();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        };

        Dexter.withContext(ImageTest.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(permissionListener).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(getString(R.string.label_need_permission));
        builder.setMessage(getString(R.string.label_permission_description));
        builder.setPositiveButton(getString(R.string.label_go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(R.string.label_cancel), (dialog, which) -> {
            dialog.cancel();
            showPermissionRequiredDialogue();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(getString(R.string.label_package_text), getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showPermissionRequiredDialogue() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(getString(R.string.label_need_permission));
        builder.setMessage(getString(R.string.label_permission_description));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.okay), (dialog, which) -> {
            dialog.cancel();
            checkForPermission();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void copyFiles() {
        sourcePath = new File(Environment.getExternalStoragePublicDirectory("Cropin"), "farmer/");
        destinationPath = ImageTest.this.getExternalFilesDir("");
        tvSourcePath.setText(sourcePath.getAbsolutePath());
        tvDestinationPath.setText(destinationPath.getAbsolutePath());
        FileUtil.FileCopyListener mListener = fileName -> {
            Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
        };
        FileUtil.copyFileOrDirectory(sourcePath.getAbsolutePath(), destinationPath.getAbsolutePath(), mListener);

    }


}