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
            imageView.setContentDescription(imageView.getContext().getString(R.string.article_image_description, item.getTitle()));
        } else {
            imageView.setImageDrawable(null);
            imageView.setContentDescription("");
        }

        itemView.setOnClickListener(v -> selectedCallback.onItemSelected(item, imageView));
    }
}

