package com.gdogaru.spacescoop.view.main.scoops.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.db.model.ArticlePreview;
import com.gdogaru.spacescoop.view.main.scoops.ItemSelectedCallback;


public class ArticleListAdapter extends PagedListAdapter<ArticlePreview, ArticlePreviewVH> {

    private final LayoutInflater inflater;
    private final ImageDownloader imageDownloader;
    private final String lang;
    private final ItemSelectedCallback selectedCallback;


    public ArticleListAdapter(Activity context, String lang, ImageDownloader imageDownloader, ItemSelectedCallback selectedCallback) {
        super(new ArticleDiffCallback());
        inflater = LayoutInflater.from(context);
        this.imageDownloader = imageDownloader;
        this.lang = lang;
        this.selectedCallback = selectedCallback;
    }

    @NonNull
    @Override
    public ArticlePreviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ArticlePreviewVH.create(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlePreviewVH holder, int position) {
        ArticlePreview item = getItem(position);
        holder.bind(item, lang, imageDownloader, selectedCallback);
    }
}
