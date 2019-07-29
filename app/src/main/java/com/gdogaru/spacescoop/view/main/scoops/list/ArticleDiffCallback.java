package com.gdogaru.spacescoop.view.main.scoops.list;

import androidx.annotation.NonNull;

import com.gdogaru.spacescoop.db.model.ArticlePreview;

public class ArticleDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<ArticlePreview> {
    @Override
    public boolean areItemsTheSame(@NonNull ArticlePreview oldItem, @NonNull ArticlePreview newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ArticlePreview oldItem, @NonNull ArticlePreview newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
