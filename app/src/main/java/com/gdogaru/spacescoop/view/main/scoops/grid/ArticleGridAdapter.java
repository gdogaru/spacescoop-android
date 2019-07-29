package com.gdogaru.spacescoop.view.main.scoops.grid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.db.model.ArticleThumb;
import com.gdogaru.spacescoop.view.main.scoops.ItemSelectedCallback;

/**
 * Created by Bogdan Balta @ TAGonSoft
 */
public class ArticleGridAdapter extends PagedListAdapter<ArticleThumb, ArticleThumbVH> {

    private final LayoutInflater inflater;
    private final ImageDownloader imageDownloader;
    private final String lang;
    private final ItemSelectedCallback selectedCallback;


    public ArticleGridAdapter(Activity context, String lang, ImageDownloader imageDownloader, ItemSelectedCallback selectedCallback) {
        super(new ArticleThumbDiffCallback());
        inflater = LayoutInflater.from(context);
        this.imageDownloader = imageDownloader;
        this.lang = lang;
        this.selectedCallback = selectedCallback;
    }

    @NonNull
    @Override
    public ArticleThumbVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ArticleThumbVH.create(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleThumbVH holder, int position) {
        ArticleThumb item = getItem(position);
        holder.bind(item, lang, imageDownloader, selectedCallback);
    }
}

