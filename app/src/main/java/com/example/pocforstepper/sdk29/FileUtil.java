package com.example.pocforstepper.sdk29;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

public final class FileUtil {

    private FileUtil() {
        // This class is not publicly instantiable
    }

    interface FileCopyListener {
        void fileCopyDone(String name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File getOutputMediaFile() throws IOException {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory("Satyajit"), "testFolder");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".png");
        return mediaFile;
    }

    public static String getOutputFolderName(Context context) {
        File mediaStorageDir = new File(context.getFilesDir(), "UploadVideo");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return mediaStorageDir.getPath();
    }

    public static void deleteFile(Context applicationContext, String uri) {
        try {
            File rawFile = new File(Uri.parse(uri).getPath());
            boolean isDeletedRawFile = rawFile.delete();
            if (!isDeletedRawFile) {
                rawFile.getCanonicalFile().delete();
            }
            if (rawFile.exists()) {
                applicationContext.deleteFile(rawFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation, FileCopyListener mListener)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {
                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]), mListener);
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }


    public static void copyFileOrDirectory(String srcDir, String dstDir, FileCopyListener mListener) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    File srcFile = new File(src, files[i]);
                    String src1 = srcFile.getPath();
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1, mListener);
                }
                mListener.fileCopyDone(src.getName());
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


}

