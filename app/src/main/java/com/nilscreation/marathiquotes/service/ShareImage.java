package com.nilscreation.marathiquotes.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ShareImage {
    Context context;
    Bitmap bitmap;
    String ogText;

    public ShareImage(Context context, Bitmap bitmap, String ogText) {
        this.context = context;
        this.bitmap = bitmap;
        this.ogText = ogText;
    }

    public void shareImageandText() {
        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_STREAM, uri);

        intent.putExtra(Intent.EXTRA_TEXT, ogText
                + "\n\n" +
                "दररोज असेच नवनवीन मराठी स्टेटस पाहण्यासाठी आत्ताच ॲप डाऊनलोड करा." +
                "\nhttps://play.google.com/store/apps/details?id=" + context.getApplicationContext().getPackageName());

        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        intent.setType("image/png");

        context.startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File imagefolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "MarathiStatus.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(context, "com.nilscreation.marathiquotes", file);
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}
