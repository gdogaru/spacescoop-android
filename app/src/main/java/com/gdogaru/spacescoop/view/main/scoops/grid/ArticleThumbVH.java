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

import androidx.recyclerview.widget.RecyclerView;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.databinding.MainScoopsGridItemBinding;
import com.gdogaru.spacescoop.db.model.ArticleThumb;
import com.gdogaru.spacescoop.view.main.scoops.ItemSelectedCallback;


public class ArticleThumbVH extends RecyclerView.ViewHolder {

    private final MainScoopsGridItemBinding binding;

    public ArticleThumbVH(MainScoopsGridItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static ArticleThumbVH create(LayoutInflater inflater) {
        return new ArticleThumbVH(MainScoopsGridItemBinding.inflate(inflater));
    }

    public void bind(ArticleThumb item, String lang, ImageDownloader imageDownloader, ItemSelectedCallback selectedCallback) {
        if (item == null) return;

        if (item.getHeadImageUrl() != null) {
            imageDownloader.displayThumb(item.getHeadImageUrl(), binding.image);
            binding.image.setContentDescription(binding.image.getContext().getString(R.string.article_image_description, item.getTitle()));
        } else {
            binding.image.setImageDrawable(null);
            binding.image.setContentDescription("");
        }

        itemView.setOnClickListener(v -> selectedCallback.onItemSelected(item, binding.image));
    }
}

