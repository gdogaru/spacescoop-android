/*
 * Copyright (c) 2019 Gabriel Dogaru - gdogaru@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
            imageView.setContentDescription(imageView.getContext().getString(R.string.article_image_description, item.getTitle()));
        } else {
            imageView.setImageDrawable(null);
            imageView.setContentDescription("");
        }

        itemView.setOnClickListener(v -> selectedCallback.onItemSelected(item, imageView));
    }
}

