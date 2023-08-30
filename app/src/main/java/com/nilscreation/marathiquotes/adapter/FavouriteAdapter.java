package com.nilscreation.marathiquotes.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.nilscreation.marathiquotes.Quote;
import com.nilscreation.marathiquotes.R;
import com.nilscreation.marathiquotes.model.QuoteModel;
import com.nilscreation.marathiquotes.service.MyDBHelper;
import com.nilscreation.marathiquotes.service.ShareImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MovieHolder> {

    Context context;
    List<String> quotelist;
    FragmentActivity activity;
    private InterstitialAd mInterstitialAd;
    private int mCounter = 0;
    Random r = new Random();
    String finalQuote;
    MyDBHelper myDBHelper;

    public FavouriteAdapter(Context context, List<String> quotelist, FragmentActivity activity) {
        this.context = context;
        this.quotelist = quotelist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);

        String ogQuote = quotelist.get(position);

        if (ogQuote.startsWith(String.valueOf(100))) {
            Typeface typeface = ResourcesCompat.getFont(holder.title.getContext(), R.font.nunito_extrabold);
            holder.title.setTypeface(typeface);
            holder.title.setText(ogQuote.substring(4));
        } else {
            Typeface typeface = ResourcesCompat.getFont(holder.title.getContext(), R.font.marathi50);
            holder.title.setTypeface(typeface);
            finalQuote = Quote.quote(ogQuote);
            holder.title.setText(finalQuote);
        }

//        String[] colors = {
//                "#FF0000", "#009AFA", "#FE857A", "#B95079", "#F31349", "#FD4311", "#A2A2AA", "#1D6BB6",
//                "#489D88", "#009AFA", "#B50840", "#773DDD", "#0EA860", "#10B5C8", "#1E96B9", "#015F45",
//                "#FE6963", "#0D75FA", "#46B149", "#D43B8B", "#04C1FB", "#7B61A8", "#D77051", "#A261F3"
//        };

        holder.favourite.setImageResource(R.drawable.ic_heart2);

        holder.relativeLayout.setBackgroundColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        //   holder.relativeLayout.setBackgroundColor(Color.parseColor(colors[r.nextInt(colors.length)]));

        holder.itemView.startAnimation(animation);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.relativeLayout.setBackgroundColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHelper = new MyDBHelper(holder.favourite.getContext());

                myDBHelper.deleteData(ogQuote);
                Toast.makeText(holder.likeButton.getContext(), "Removed from Favourite", Toast.LENGTH_SHORT).show();
                quotelist.remove(ogQuote);
                notifyDataSetChanged();

            }
        });

        holder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager = (ClipboardManager) holder.copyButton.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("simple text", ogQuote);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(holder.copyButton.getContext(), "Text copied", Toast.LENGTH_SHORT).show();

            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (holder.saveButton.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
                        ActivityCompat.requestPermissions((Activity) holder.saveButton.getContext(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        return;
                    }
                }

                Bitmap bitmap = Bitmap.createBitmap(holder.relativeLayout.getWidth(), holder.relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                holder.relativeLayout.draw(canvas);

                saveImageToGallery(bitmap);
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = Bitmap.createBitmap(holder.relativeLayout.getWidth(), holder.relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                holder.relativeLayout.draw(canvas);
                ShareImage shareImage = new ShareImage(context, bitmap, ogQuote);
                shareImage.shareImageandText();
            }
        });
    }

    private void saveImageToGallery(Bitmap imageBitmap) {
        String savedImagePath;
        String imageFileName = "MarathiStatus_" + System.currentTimeMillis() + ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PhotoEditor");
            Uri imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try (OutputStream os = activity.getContentResolver().openOutputStream(imageUri)) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                Toast.makeText(activity, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
            savedImagePath = imageUri.toString();
        } else {
            File storageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Sunglasses_PhotoEditor");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Toast.makeText(activity, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        galleryAddPic(savedImagePath);
//        EditActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    private void mInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, "ca-app-pub-9137303962163689/2088238601", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
//                        Toast.makeText(activity, "loaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public int getItemCount() {
        return quotelist.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        CardView likeButton, copyButton, shareButton, saveButton;
        TextView title;
        ConstraintLayout constraintLayout;
        RelativeLayout relativeLayout;
        ImageView favourite;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.main_title);
            likeButton = itemView.findViewById(R.id.likebutton);
            copyButton = itemView.findViewById(R.id.copybutton);
            shareButton = itemView.findViewById(R.id.sharebutton);
            saveButton = itemView.findViewById(R.id.savebutton);
            constraintLayout = itemView.findViewById(R.id.main_layout);
            relativeLayout = itemView.findViewById(R.id.content);
            favourite = itemView.findViewById(R.id.imgfav);
        }
    }
}
