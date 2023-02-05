package com.codewithmuddasir.videodownloader;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.codewithmuddasir.videodownloader.Util.FACEBOOK_DIR;
import static com.codewithmuddasir.videodownloader.Util.ROOT_DIR_FACEBOOK;
import static com.codewithmuddasir.videodownloader.Util.createAppFolder;
import static com.codewithmuddasir.videodownloader.Util.downloadFB;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codewithmuddasir.videodownloader.databinding.ActivityFacebookBinding;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class FacebookActivity extends AppCompatActivity {

    private ActivityFacebookBinding binding;
    private FacebookActivity activity;

    private String videoUrl;
    private ClipboardManager clipBoard;
    private String strName = "facebook";
    private String strNameSecond = "fb";

    private final String TAG = "Facebook Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook);

        activity = this;

        createAppFolder();
        initViews();
//        InterstitialAdsINIT();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        pasteText();
    }
    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.btnPaste.setOnClickListener(view -> {
            pasteText();
        });

        binding.btnDownload.setOnClickListener(v -> {
            String ll = binding.facebookUrl.getText().toString();
            if (ll.equals("")) {
                Util.setToast(activity, "Enter Video Url");
            } else if (!Patterns.WEB_URL.matcher(ll).matches()) {
                Util.setToast(activity, "Enter valid video url");
            } else {
                getFacebookData();
//                showInterstitialAds();
            }

//            showInterstitialAds();
        });

    }


    private void getFacebookData() {
        try {
            createAppFolder();
            URL url = new URL(binding.facebookUrl.getText().toString());
            String host = url.getHost();
            if (host.contains(strName) || host.contains(strNameSecond)) {
                new CallGetFacebookData().execute(binding.facebookUrl.getText().toString());
//                showInterstitialAds();
            } else {
                Util.setToast(activity, "Enter valid video url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pasteText() {
        try {
            binding.facebookUrl.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
//            copyIntent=MainActivity.extractLinks(copyIntent);
            if (copyIntent == null || copyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {
                    Log.d(TAG, "PasteText");
                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(strName)) {
                        binding.facebookUrl.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    } else if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(strNameSecond)) {
                        binding.facebookUrl.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains(strName)) {
                        binding.facebookUrl.setText(item.getText().toString());
                    } else if (item.getText().toString().contains(strNameSecond)) {
                        binding.facebookUrl.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains(strName)) {
                    binding.facebookUrl.setText(copyIntent);
                } else if (copyIntent.contains(strNameSecond)) {
                    binding.facebookUrl.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CallGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;

        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return facebookDoc;
        }

        @Override
        protected void onPostExecute(Document result) {
            try {
                videoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                if (!videoUrl.equals("")) {
                    downloadFB(videoUrl, FACEBOOK_DIR, activity, "facebook_" + System.currentTimeMillis() + ".mp4");
                    videoUrl = "";
//                    showInterstitialAds();
                    binding.facebookUrl.setText("");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100 && resultCode == RESULT_OK) {
                getFacebookUserData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getFacebookUserData() {
        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
                .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
                .addHeaders(
                        "user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
                )
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter(
                        "variables",
                        "{\"bucketsCount\":200,\"initialBucketID\":null,\"pinnedIDs\":[\"\"],\"scale\":3}"
                )
                .addBodyParameter("doc_id", "2893638314007950")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("JsonResp- " + response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showAds() {
    }
}