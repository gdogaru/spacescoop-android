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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.databinding.MainScoopsListItemBinding;
import com.gdogaru.spacescoop.db.model.ArticlePreview;
import com.gdogaru.spacescoop.util.FormatUtil;
import com.gdogaru.spacescoop.view.main.scoops.ItemSelectedCallback;


public class ArticlePreviewVH extends RecyclerView.ViewHolder {
    private final MainScoopsListItemBinding binding;

    public ArticlePreviewVH(@NonNull MainScoopsListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static ArticlePreviewVH create(LayoutInflater inflater) {
        return new ArticlePreviewVH(MainScoopsListItemBinding.inflate(inflater));
    }

    public void bind(ArticlePreview item, String lang, ImageDownloader imageDownloader, ItemSelectedCallback selectedCallback) {
        if (item == null) return;

        binding.newsTitleText.setText(item.getTitle());
        binding.dateText.setText(FormatUtil.formatForUI(item.getPublishDate(), lang));
        binding.newsFirstWords.setText(item.getPreviewText());

        if (item.getHeadImageUrl() != null) {
            imageDownloader.displayThumb(item.getHeadImageUrl(), binding.newsImage);
            binding.newsImage.setContentDescription(binding.newsImage.getContext().getString(R.string.article_image_description, item.getTitle()));
        } else {
            binding.newsImage.setImageDrawable(null);
            binding.newsImage.setContentDescription("");
        }

        itemView.setOnClickListener(v -> selectedCallback.onItemSelected(item, binding.newsImage));
    }
}

