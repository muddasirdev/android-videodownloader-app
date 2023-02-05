package com.codewithmuddasir.videodownloader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codewithmuddasir.videodownloader.R;
import com.codewithmuddasir.videodownloader.Util;
import com.codewithmuddasir.videodownloader.databinding.WhatsappLayoutItemBinding;
import com.codewithmuddasir.videodownloader.model.WhatsappStatusModel;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WhatsappAdapter extends RecyclerView.Adapter<WhatsappAdapter.ViewHolder> {

    private ArrayList<WhatsappStatusModel> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private String saveFilePath = Util.ROOT_DIR_WHATSAPP + "/";

    public WhatsappAdapter(ArrayList<WhatsappStatusModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.whatsapp_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WhatsappAdapter.ViewHolder holder, int position) {
        WhatsappStatusModel item = list.get(position);

        if (item.getUri().toString().endsWith(".mp4"))
            holder.binding.playButton.setVisibility(View.VISIBLE);
        else
            holder.binding.playButton.setVisibility(View.GONE);

        Glide.with(context).load(item.getPath()).into(holder.binding.statusImage);

        holder.binding.download.setOnClickListener(view -> {
            Util.createAppFolder();

            final File file = new File(item.getPath());
            File destFile = new File(saveFilePath);

            try {
                FileUtils.copyFileToDirectory(file, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(context, "Saved to: " + saveFilePath, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WhatsappLayoutItemBinding binding;

        public ViewHolder(WhatsappLayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
