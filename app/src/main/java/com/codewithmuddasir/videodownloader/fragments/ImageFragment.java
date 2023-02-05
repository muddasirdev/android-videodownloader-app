package com.codewithmuddasir.videodownloader.fragments;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codewithmuddasir.videodownloader.R;
import com.codewithmuddasir.videodownloader.adapter.WhatsappAdapter;
import com.codewithmuddasir.videodownloader.databinding.FragmentImageBinding;
import com.codewithmuddasir.videodownloader.databinding.WhatsappLayoutItemBinding;
import com.codewithmuddasir.videodownloader.model.WhatsappStatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFragment extends Fragment {

    private FragmentImageBinding binding;
    private ArrayList<WhatsappStatusModel> list;
    private WhatsappAdapter whatsappAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);

        list = new ArrayList<>();
        getData();

        binding.refresh.setOnRefreshListener(() -> {
            list = new ArrayList<>();
            getData();

            binding.refresh.setRefreshing(false);
        });

        return binding.getRoot();
    }

    private void getData() {
        WhatsappStatusModel model;

        String path=  Build.VERSION.SDK_INT < 30  ? "WhatsApp/Media/.Statuses":"Android/media/com.whatsapp/WhatsApp/Media/.Statuses";


        String targetDirWhatsApp = Environment.getExternalStorageDirectory() + File.separator + path;

        Log.i("ImageFragment", targetDirWhatsApp);

        File targetFiles = new File(targetDirWhatsApp);

        File[] allFiles = targetFiles.listFiles();

//        String targetDirBusiness = Environment.getExternalStorageDirectory() + (Build.VERSION.SDK_INT <= 30 ? "/WhatsApp/Media/.statuses" : "Android/media/com.whatsapp.w4b/WhatsApp/Media/.statuses");
//
//        File targetFilesBusiness = new File(targetDirBusiness);
//
//        File[] allFilesBusiness = targetFilesBusiness.listFiles();

        Arrays.sort(allFiles, (file, t1) -> {
            if (file.lastModified() > t1.lastModified())
                return -1;
            else if (file.lastModified() < t1.lastModified())
                return +1;
            else
                return 0;
        });

        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];

            if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                model = new WhatsappStatusModel("whats" + i, Uri.fromFile(file), allFiles[i].getAbsolutePath(), file.getName());

                list.add(model);
            }
        }

//        Arrays.sort(allFilesBusiness, (file, t1) -> {
//            if (file.lastModified() > t1.lastModified())
//                return -1;
//            else if (file.lastModified() < t1.lastModified())
//                return +1;
//            else
//                return 0;
//        });
//
//        for (int i = 0; i < allFilesBusiness.length; i++) {
//            File file = allFilesBusiness[i];
//
//            if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
//                model = new WhatsappStatusModel("whatsBusiness" + i, Uri.fromFile(file), allFilesBusiness[i].getAbsolutePath(), file.getName());
//
//                list.add(model);
//            }
//        }

        whatsappAdapter = new WhatsappAdapter(list, getActivity());

        binding.whatsappRecycler.setAdapter(whatsappAdapter);
    }
}