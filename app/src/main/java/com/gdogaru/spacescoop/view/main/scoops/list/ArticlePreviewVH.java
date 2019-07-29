package com.gdogaru.spacescoop.view.main.scoops.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.db.model.ArticlePreview;
import com.gdogaru.spacescoop.util.FormatUtil;
import com.gdogaru.spacescoop.view.main.scoops.ItemSelectedCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlePreviewVH extends RecyclerView.ViewHolder {
    @BindView(R.id.newsTitleText)
    TextView titleView;

    @BindView(R.id.dateText)
    TextView dateView;

    @BindView(R.id.newsFirstWords)
    TextView description;

    @BindView(R.id.newsImage)
    ImageView imageView;


    public ArticlePreviewVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static ArticlePreviewVH create(LayoutInflater inflater, ViewGroup parent) {
        return new ArticlePreviewVH(inflater.inflate(R.layout.main_scoops_list_item, parent, false));
    }

    public void bind(ArticlePreview item, String lang, ImageDownloader imageDownloader, ItemSelectedCallback selectedCallback) {
        if (item == null) return;

        titleView.setText(item.getTitle());
        dateView.setText(FormatUtil.formatForUI(item.getPublishDate(), lang));
        description.setText(item.getPreviewText());

        if (item.getHeadImageUrl() != null) {
            imageDownloader.displayThumb(item.getHeadImageUrl(), imageView);
        } else {
            imageView.setImageDrawable(null);
        }

        itemView.setOnClickListener(v -> selectedCallback.onItemSelected(item, imageView));
    }
}

