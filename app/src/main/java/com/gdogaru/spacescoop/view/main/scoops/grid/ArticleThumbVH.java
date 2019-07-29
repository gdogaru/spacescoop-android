package com.gdogaru.spacescoop.view.main.scoops.grid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.db.model.ArticleThumb;
import com.gdogaru.spacescoop.view.main.scoops.ItemSelectedCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleThumbVH extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    ImageView imageView;

    public ArticleThumbVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static ArticleThumbVH create(LayoutInflater inflater, ViewGroup parent) {
        return new ArticleThumbVH(inflater.inflate(R.layout.main_scoops_grid_item, parent, false));
    }

    public void bind(ArticleThumb item, String lang, ImageDownloader imageDownloader, ItemSelectedCallback selectedCallback) {
        if (item == null) return;

        if (item.getHeadImageUrl() != null) {
            imageDownloader.displayThumb(item.getHeadImageUrl(), imageView);
        } else {
            imageView.setImageDrawable(null);
        }

        itemView.setOnClickListener(v -> selectedCallback.onItemSelected(item, imageView));
    }
}

