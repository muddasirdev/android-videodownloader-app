package com.codewithmuddasir.videodownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

public class Util {
    public static File ROOT_DIR_WHATSAPP = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Video Downloader/Whatsapp");

    public static String FACEBOOK_DIR = "/Video Downloader/Facebook";

    public static File ROOT_DIR_FACEBOOK = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + FACEBOOK_DIR);

    public static String YOUTUBE_DIR = "/Video Downloader/Youtube";

    public static File ROOT_DIR_YOUTUBE = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + YOUTUBE_DIR);

    public static void createAppFolder() {
        if (!ROOT_DIR_WHATSAPP.exists()) {
            ROOT_DIR_WHATSAPP.mkdir();
        }

        if (!ROOT_DIR_FACEBOOK.exists()) {
            ROOT_DIR_FACEBOOK.mkdir();
        }

        if (!ROOT_DIR_YOUTUBE.exists()) {
            ROOT_DIR_YOUTUBE.mkdir();
        }
    }

    public static void downloadFB(String downloadPath, String destinationPath, Context context, String fileName) {
        setToast(context, "Start Downloading");

        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(fileName + ""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destinationPath + fileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(Environment.DIRECTORY_DOWNLOADS + "/" + destinationPath + fileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS + "/" + destinationPath + fileName))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadYoutubeVideo(String downloadPath, String destinationPath, Context context, String fileName) {
        setToast(context, "Start Downloading");

       Uri uri = Uri.parse(downloadPath);

       DownloadManager.Request request = new DownloadManager.Request(uri);
       request.setTitle(fileName);
       request.allowScanningByMediaScanner();
       request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destinationPath + fileName);

       DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
       downloadManager.enqueue(request);
    }

    public static void setToast(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
