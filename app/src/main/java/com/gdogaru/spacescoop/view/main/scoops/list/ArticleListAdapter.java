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
        return ArticlePreviewVH.create(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlePreviewVH holder, int position) {
        ArticlePreview item = getItem(position);
        holder.bind(item, lang, imageDownloader, selectedCallback);
    }
}
