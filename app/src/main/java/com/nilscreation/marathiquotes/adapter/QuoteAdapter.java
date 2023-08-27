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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.nilscreation.marathiquotes.Quote;
import com.nilscreation.marathiquotes.service.MyDBHelper;
import com.nilscreation.marathiquotes.R;
import com.nilscreation.marathiquotes.model.QuoteModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.MovieHolder> {

    Context context;
    List<QuoteModel> quotelist;
    FragmentActivity activity;
    private InterstitialAd mInterstitialAd;
    private int mCounter = 0;
    Random r = new Random();
    String finalQuote, mTitle;
    MyDBHelper myDBHelper;

    public QuoteAdapter(Context context, List<QuoteModel> quotelist, FragmentActivity activity) {
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

        QuoteModel quoteModel = quotelist.get(position);

//        shree_dev(quoteModel.getTitle());
        finalQuote = Quote.quote(quoteModel.getTitle());

//        String[] colors = {
//                "#FF0000", "#009AFA", "#FE857A", "#B95079", "#F31349", "#FD4311", "#A2A2AA", "#1D6BB6",
//                "#489D88", "#009AFA", "#B50840", "#773DDD", "#0EA860", "#10B5C8", "#1E96B9", "#015F45",
//                "#FE6963", "#0D75FA", "#46B149", "#D43B8B", "#04C1FB", "#7B61A8", "#D77051", "#A261F3"
//        };

        holder.title.setText(finalQuote);
        Typeface typeface = ResourcesCompat.getFont(holder.title.getContext(), R.font.marathi240);
//        holder.title.setTypeface(typeface);

        holder.relativeLayout.setBackgroundColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        //   holder.relativeLayout.setBackgroundColor(Color.parseColor(colors[r.nextInt(colors.length)]));

        String text = quoteModel.getTitle();
//        readData();

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
                mTitle = quoteModel.getTitle();

                myDBHelper.deleteandAdd(mTitle);
//                holder.favourite.setImageResource(R.drawable.ic_heart2);
//                ImageViewCompat.setImageTintList(holder.favourite, ColorStateList.valueOf
//                        (ContextCompat.getColor(holder.favourite.getContext(), R.color.red)));
                Toast.makeText(holder.favourite.getContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();


//                fact.setLiked(!fact.isLiked);
//
//                if (!fact.isLiked) {
//                    myDBHelper.deleteData(mTitle);
//                    holder.favourite.setImageResource(R.drawable.ic_heart);
//                    ImageViewCompat.setImageTintList(holder.favourite, ColorStateList.valueOf
//                            (ContextCompat.getColor(holder.favourite.getContext(), R.color.ic_color)));
//                    Toast.makeText(holder.favourite.getContext(), "Removed from Favourite", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    myDBHelper.deleteandAdd(mTitle);
//                    holder.favourite.setImageResource(R.drawable.ic_heart2);
//                    ImageViewCompat.setImageTintList(holder.favourite, ColorStateList.valueOf
//                            (ContextCompat.getColor(holder.favourite.getContext(), R.color.red)));
//                    Toast.makeText(holder.favourite.getContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        holder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager = (ClipboardManager) holder.copyButton.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("simple text", text);
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

                //Replace your own action here
                String appUrl = text + " For More Health Facts & Tips download the app now " + "https://play.google.com/store/apps/details?id=" + holder.shareButton.getContext().getPackageName();

                Intent sharing = new Intent(Intent.ACTION_SEND);
                sharing.setType("text/plain");
                sharing.putExtra(Intent.EXTRA_SUBJECT, "Download Now");
                sharing.putExtra(Intent.EXTRA_TEXT, appUrl);
                holder.shareButton.getContext().startActivity(Intent.createChooser(sharing, "Share via"));

            }
        });

    }

    private void saveImageToGallery(Bitmap imageBitmap) {
        String savedImagePath;
        String imageFileName = "Photo_" + System.currentTimeMillis() + ".jpg";

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

//    private void shree_dev(String str) {
//        String str2 = "-";
//        str = str.replace(str2, str2);
//        str2 = "=";
//        str = str.replace(str2, str2);
//        str2 = "+";
//        String str3 = "(";
//        String str4 = ")";
//        str2 = "|";
//        str = str.replace(str2, str2).replace("{", str3).replace("}", str4).replace(str3, str3).replace(str4, str4).replace(str2, str2);
//        str2 = ":";
//        str = str.replace(str2, str2);
//        str2 = "!";
//        str = str.replace(str2, str2);
//        str2 = "/";
//        str = str.replace(str2, str2);
//        str2 = "?";
//        str = str.replace(str2, str2);
//        str2 = ".";
//        str = str.replace(str2, str2);
//        str2 = ",";
//
//        //MY CODE
//
//        str = str.replace(str2, str2).replace("ज्ञ", "k").replace("पूर्ण", "nyU©").replace("मार्ग", "_mJ©").replace("ऱ्य", "è`").replace("निर्माण", "{Z_©mU").replace("क्ष", "j").replace("सर्व", "gd©").replace("निर्णय", "{ZU©`").replace("कर्तव्य", "H$V©ì`").replace("वर्त", "dV©")
//                .replace("ॐ", "›").replace("रु", "é").replace("द्ध", "Õ").replace("द्य", "Ú").replace("द्व", "Û");
//        str2 = "प्र";
//        str3 = "à";
//        str4 = "फ्र";
//        String str5 = "\\«$";
//        String str6 = "द्र";
//        String str7 = "Ð";
//        String str8 = "S´>";
//        str = str.replace(str2, str3).replace("द्द", "Ô").replace("ट्ट", "Å").replace("ठ्ठ", "Ç").replace("ड्ड", "È").replace("कृ", "H¥$").replace("द्प", "X²n").replace("द्भ", "Ø").replace("ड्ढ", "–").replace("त्त", "Îm").replace("त्त्", "Îm²").replace(str4, str5).replace("ह्न", "•").replace("ह्य", "ø").replace("ह्ल", "‡").replace("ह्व", "ˆ").replace("हृ", "ö").replace("ह्म", "÷").replace("क्त", "º$").replace("द्ग", "Ò").replace(str6, str7).replace("क्क", "¸$").replace("न्न", "Þ").replace("ट्ठ", "Æ").replace("झ्म", "Â_").replace("स्त्र", "ñÌ").replace("अ", "A").replace("आ", "Am").replace("इ", "B").replace("ई", "B©").replace("उ", "C").replace("ऊ", "D$").replace("ऋ", "F$").replace("ए", "E").replace("ऐ", "Eo").replace("ऑ", "Am°").replace("ओ", "Amo").replace("औ", "Am¡").replace("क्र", "H«$").replace("ख्र", "¼").replace("ग्र", "J«").replace("घ्र", "K«").replace("च्र", "M«").replace("छ्र", "N´").replace("ज्र", "O«").replace("ञ्र", "Äm«").replace("झ्र", "Ã").replace("ट्र", "Q´>").replace("ठ्र", "R´>").replace("ड्र", str8).replace("ड़्र", str8).replace("ढ्र", "T´>").replace("ण्र", "U«");
//        String str9 = "त्र";
//        str8 = "Ì";
//        str = str.replace(str9, str8).replace("थ्र", "W«").replace(str6, str7).replace("ध्र", "Y«").replace("न्र", "Z«").replace(str2, str3).replace(str4, str5).replace("ब्र", "~«").replace("भ्र", "^«").replace("म्र", "_«").replace("य्र", "`«").replace("र्र", "a©").replace("र्य", "`©").replace("ल्र", "c«").replace("ळ्र", "i´").replace("व्र", "d«");
//        str2 = "श्र";
//        str = str.replace(str2, "l").replace("ष्र", "f«").replace("स्र", "ò").replace("ह्र", "õ").replace("र्क्षि", "{j©").replace("र्त्रि", "{Ì©").replace("र्ज्ञि", "{k©").replace("र्श्रि", "{l©").replace("र्कि", "{H$©").replace("र्खि", "{I©").replace("र्गि", "{J©").replace("र्घि", "{K©").replace("र्चि", "{M©").replace("र्छि", "{N©").replace("र्जि", "{O©").replace("र्झि", "{P©").replace("र्ञि", "{Äm©").replace("र्टि", "{Q>©").replace("र्ठि", "{R>©").replace("र्डि", "{S>©").replace("र्ड़ि", "{S>©").replace("र्ढि", "{T>©").replace("र्णि", "{U©").replace("र्ति", "{V©").replace("र्थि", "{W©").replace("र्दि", "{X©").replace("र्धि", "{Y©").replace("र्नि", "{Z©").replace("र्पि", "{n©").replace("र्फि", "{\\$©").replace("र्बि", "{~©").replace("र्भि", "{^©").replace("र्मि", "{_©").replace("र्यि", "{`©").replace("र्रि", "{a©").replace("र्लि", "{c©").replace("र्ळि", "{i©").replace("र्वि", "{d©").replace("र्शि", "{e©").replace("र्सि", "{g©").replace("र्षि", "{f©").replace("र्हि", "{h©").replace("र्क्षी", "ju").replace("र्त्री", "Ìu").replace("र्ज्ञी", "ku").replace("र्श्री", "lu").replace("र्की", "H$u").replace("र्खी", "Iu").replace("र्गी", "Ju").replace("र्घी", "Ku").replace("र्ची", "Mu").replace("र्छी", "Nu").replace("र्जी", "Ou").replace("र्ञी", "Pu").replace("र्झी", "Ämu").replace("र्टी", "Q>u").replace("र्ठी", "R>u").replace("र्डी", "S>u").replace("र्ड़ी", "T>u").replace("र्ढी", "T>u").replace("र्णी", "Uu").replace("र्ती", "Vu").replace("र्थी", "Wu").replace("र्दी", "Xu").replace("र्धी", "Yu").replace("र्नी", "Zu").replace("र्पी", "nu").replace("र्फी", "\\$u").replace("र्बी", "~u").replace("र्भी", "^u").replace("र्मी", "_u").replace("र्यी", "`u").replace("र्री", "au").replace("र्ली", "cu").replace("र्ळी", "iu").replace("र्वी", "du").replace("र्शी", "eu").replace("र्सी", "gu").replace("र्षी", "fu").replace("र्ही", "hu").replace("र्क्षे", "j}").replace("र्त्रे", "Ì}").replace("र्ज्ञे", "k}").replace("र्श्रे", "l}").replace("र्के", "H$}").replace("र्खे", "I}").replace("र्गे", "J}").replace("र्घे", "K}").replace("र्चे", "M}").replace("र्छे", "N}").replace("र्जे", "O}").replace("र्झे", "P}").replace("र्ञे", "Äm}").replace("र्टे", "Q>}").replace("र्ठे", "R>}").replace("र्डे", "S>}").replace("र्ड़े", "S>}").replace("र्ढे", "T>}").replace("र्णे", "U}").replace("र्ते", "V}").replace("र्थे", "W}").replace("र्दे", "X}").replace("र्धे", "Y}").replace("र्ने", "Z}").replace("र्पे", "n}").replace("र्फे", "\\$}").replace("र्बे", "~}").replace("र्भे", "^}").replace("र्मे", "_}").replace("र्ये", "`}").replace("र्रे", "a}").replace("र्ले", "c}").replace("र्ळे", "i}").replace("र्वे", "d}").replace("र्शे", "e}").replace("र्से", "g}").replace("र्षे", "f}").replace("र्हे", "h}").replace("र्क्षै", "j£").replace("र्त्रै", "Ì£").replace("र्ज्ञै", "k£").replace("र्श्रै", "l£").replace("र्कै", "H$£").replace("र्खै", "I£").replace("र्गै", "J£").replace("र्घै", "K£").replace("र्चै", "M£").replace("र्छै", "N£").replace("र्जै", "O£").replace("र्झै", "P£").replace("र्ञै", "Äm£").replace("र्टै", "Q>£").replace("र्ठै", "R>£").replace("र्डै", "S>£").replace("र्ड़ै", "S>£").replace("र्ढै", "T>£").replace("र्णै", "U£").replace("र्तै", "V£").replace("र्थै", "W£").replace("र्दै", "X£").replace("र्धै", "Y£").replace("र्नै", "Z£").replace("र्पै", "n£").replace("र्फै", "\\$£").replace("र्बै", "~£").replace("र्भै", "^£").replace("र्मै", "_£").replace("र्यै", "`£").replace("र्रै", "a£").replace("र्लै", "c£").replace("र्ळै", "i£").replace("र्वै", "d£").replace("र्शै", "e£").replace("र्सै", "g£").replace("र्षै", "f£").replace("र्है", "h£").replace("र्क्षो", "jm}").replace("र्त्रो", "Ìm}").replace("र्ज्ञो", "km}").replace("र्श्रो", "lm}").replace("र्को", "H$m}").replace("र्खो", "Im}").replace("र्गो", "Jm}").replace("र्घो", "Km}").replace("र्चो", "Mm}").replace("र्छो", "Nm}").replace("र्जो", "Om}").replace("र्झो", "Pm}").replace("र्ञो", "Ämm}").replace("र्टो", "Q>m}").replace("र्ठो", "R>m}").replace("र्डो", "S>m}").replace("र्ड़ो", "S>m}").replace("र्ढो", "T>m}").replace("र्णो", "Um}").replace("र्तो", "Vm}").replace("र्थो", "Wm}").replace("र्दो", "Xm}").replace("र्धो", "Ym}").replace("र्नो", "Zm}").replace("र्पो", "nm}").replace("र्फो", "\\$m}").replace("र्बो", "~m}").replace("र्भो", "^m}").replace("र्मो", "_m}").replace("र्यो", "`m}").replace("र्रो", "am}").replace("र्लो", "cm}").replace("र्ळो", "im}").replace("र्वो", "dm}").replace("र्शो", "em}").replace("र्सो", "gm}").replace("र्षो", "fm}").replace("र्हो", "hm}").replace("र्क्षौ", "jm£").replace("र्त्रौ", "Ìm£").replace("र्ज्ञौ", "km£").replace("र्श्रौ", "lm£").replace("र्कौ", "H$m£").replace("र्खौ", "Im£").replace("र्गौ", "Jm£").replace("र्घौ", "Km£").replace("र्चौ", "Mm£").replace("र्छौ", "Nm£").replace("र्जौ", "Om£").replace("र्झौ", "Pm£").replace("र्ञौ", "Ämm£").replace("र्टौ", "Q>m£").replace("र्ठौ", "R>m£").replace("र्डौ", "S>m£").replace("र्ड़ौ", "S>m£").replace("र्ढौ", "T>m£").replace("र्णौ", "Um£").replace("र्तौ", "Vm£").replace("र्थौ", "Wm£").replace("र्दौ", "Xm£").replace("र्धौ", "Ym£").replace("र्नौ", "Zm£").replace("र्पौ", "nm£").replace("र्फौ", "\\$m£").replace("र्बौ", "~m£").replace("र्भौ", "^m£").replace("र्मौ", "_m£").replace("र्यौ", "`m£").replace("र्रौ", "am£").replace("र्लौ", "cm£").replace("र्ळौ", "im£").replace("र्वौ", "dm£").replace("र्शौ", "em£").replace("र्सौ", "gm£").replace("र्षौ", "fm£").replace("र्हौ", "hm£").replace("क्षि", "{j").replace("त्रि", "{Ì").replace("ज्ञि", "{k").replace("श्रि", "{l").replace("कि", "{H$").replace("खि", "{I").replace("गि", "{J").replace("घि", "{K").replace("चि", "{M").replace("छि", "{N").replace("जि", "{O").replace("झि", "{P").replace("ञि", "{Äm").replace("टि", "{Q>").replace("ठि", "{R>").replace("डि", "{S>").replace("र्ड़ि", "{S>").replace("ढि", "{T>").replace("णि", "{U").replace("ति", "{V").replace("थि", "{W").replace("दि", "{X").replace("धि", "{Y").replace("नि", "{Z").replace("पि", "{n").replace("फि", "{\\$").replace("बि", "{~").replace("भि", "{^").replace("मि", "{_").replace("यि", "{`").replace("रि", "{a").replace("लि", "{c").replace("ळि", "{i").replace("वि", "{d").replace("शि", "{e").replace("सि", "{g").replace("षि", "{f").replace("हि", "{h").replace("क्ष्", "ú").replace("त्र्", "Í").replace("ज्ञ्", "k²").replace("श्र्", "û").replace("क्", "H²$").replace("ख्", "»").replace("ग्", "½").replace("घ्", "¿").replace("च्", "À").replace("छ्", "N²").replace("ज्", "Á").replace("झ्", "Â").replace("ञ्", "Ä").replace("ट्", "Q>²").replace("ठ्", "R>²").replace("ड्", "S>²").replace("ड़्", "S>²").replace("ढ्", "T>²").replace("ण्", "Ê").replace("त्", "Ë").replace("थ्", "Ï").replace("द्", "X²").replace("ध्", "Ü").replace("न्", "Ý").replace("प्", "ß").replace("फ्", "â").replace("ब्", "ã").replace("भ्", "ä").replace("म्", "å").replace("य्", "æ").replace("र्", "a²").replace("ल्", "ë").replace("ळ्", "i²").replace("व्", "ì").replace("श्", "í").replace("स्", "ñ").replace("ष्", "î").replace("ह्", "ô").replace(str9, str8).replace(str2, "l").replace("क", "H$").replace("ख", "I").replace("ग", "J").replace("घ", "K").replace("च", "M").replace("छ", "N").replace("ज", "O").replace("झ", "P").replace("ञ", "Äm").replace("ट", "Q>").replace("ठ", "R>").replace("ड़", "S>").replace("ड", "S>").replace("ढ", "T>").replace("ण", "U").replace("त", "V").replace("थ", "W").replace("द", "X").replace("ध", "Y").replace("न", "Z").replace("प", "n").replace("फ", "\\$").replace("ब", "~").replace("भ", "^").replace("म", "_").replace("य", "`").replace("र", "a").replace("ल", "c").replace("ळ", "i").replace("व", "d").replace("श", "e").replace("स", "g").replace("ष", "f").replace("ह", "h").replace("ा", "m").replace("ि", "[").replace("ी", "r").replace("ु", "w").replace("ू", "y").replace("ृ", "¥").replace("े", "o").replace("ै", "¡").replace("ो", "mo").replace("ौ", "m¡").replace("ॉ", "m°").replace("ँ", "±").replace("ं", "§").replace("़", "").replace("ः", "…").replace("H²${", "{H²$").replace("»{", "{»").replace("½{", "{½").replace("¿{", "{¿").replace("À{", "{À").replace("Á{", "{Á").replace("Â{", "{Â").replace("Ê{", "{Ê").replace("Ë{", "{Ë").replace("Ï{", "{Ï").replace("Ü{", "{Ü").replace("Ý{", "{Ý").replace("ß{", "{ß").replace("â{", "{â").replace("ã{", "{ã").replace("ä{", "{ä").replace("å{", "{å").replace("æ{", "{æ").replace("a²{", "{a²").replace("ë{", "{ë").replace("ì{", "{ì").replace("í{", "{í").replace("ñ{", "{ñ").replace("î{", "{î").replace("ô{", "{ô");
//
//        title = str;
//    }

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
