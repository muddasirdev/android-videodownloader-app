package com.codewithmuddasir.videodownloader;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

import static com.codewithmuddasir.videodownloader.Util.FACEBOOK_DIR;
import static com.codewithmuddasir.videodownloader.Util.YOUTUBE_DIR;
import static com.codewithmuddasir.videodownloader.Util.createAppFolder;
import static com.codewithmuddasir.videodownloader.Util.downloadFB;
import static com.codewithmuddasir.videodownloader.Util.downloadYoutubeVideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codewithmuddasir.videodownloader.databinding.ActivityFacebookBinding;
import com.codewithmuddasir.videodownloader.databinding.ActivityYoutubeBinding;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YoutubeActivity extends AppCompatActivity {

    private ActivityYoutubeBinding binding;
    private YoutubeActivity activity;
    private ClipboardManager clipBoard;

    private final String TAG = "Youtube Activity";

    private String strName = "youtube";
    private String strName2 = "youtu.be";

    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_youtube);

        activity = this;

        createAppFolder();
        initViews();
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

        YouTubeUriExtractor youTubeUriExtractor = new YouTubeUriExtractor(this) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;
                    try {
                        downloadUrl = ytFiles.get(itag).getUrl();

                        if (downloadUrl != null) {
                            String fileName = "";

                            if (videoTitle.length() > 55) {
                                fileName = videoTitle.substring(0, 55) + "." + ytFiles.get(itag).getFormat().getExt();
                            } else {
                                fileName = videoTitle + "." + ytFiles.get(itag).getFormat().getExt();
                            }

                            fileName = fileName.replaceAll("[\\\\><\"|*?%:#/]", "");

                            downloadYoutubeVideo(downloadUrl, YOUTUBE_DIR, activity, fileName);
                        }
                    } catch (Exception ex) {
                        Util.setToast(activity, "Downloading URl could not be fetched");
                    }
                }
            }
        };

        binding.btnDownload.setOnClickListener(v -> {
            createAppFolder();
            String url = binding.youtubeUrl.getText().toString();

            if (url != null) {
                youTubeUriExtractor.extract(url);
            }
        });

    }

    private void pasteText() {
        try {
            binding.youtubeUrl.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
//            copyIntent=MainActivity.extractLinks(copyIntent);
            if (copyIntent == null || copyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {
                    Log.d(TAG, "PasteText");
                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(strName)) {
                        binding.youtubeUrl.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    } else if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(strName2)) {
                        binding.youtubeUrl.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains(strName)) {
                        binding.youtubeUrl.setText(item.getText().toString());
                    } else if (item.getText().toString().contains(strName2)) {
                        binding.youtubeUrl.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains(strName)) {
                    binding.youtubeUrl.setText(copyIntent);
                } else if (copyIntent.contains(strName2)) {
                    binding.youtubeUrl.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}