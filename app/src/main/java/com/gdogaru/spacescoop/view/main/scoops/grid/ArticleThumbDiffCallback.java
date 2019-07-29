package com.gdogaru.spacescoop.view.main.scoops.grid;

import androidx.annotation.NonNull;

import com.gdogaru.spacescoop.db.model.ArticleThumb;

public class ArticleThumbDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<ArticleThumb> {
    @Override
    public boolean areItemsTheSame(@NonNull ArticleThumb oldItem, @NonNull ArticleThumb newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ArticleThumb oldItem, @NonNull ArticleThumb newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
