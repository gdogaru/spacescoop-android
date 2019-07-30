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

package com.gdogaru.spacescoop.view.article;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ArticlesPagerAdapter extends FragmentStatePagerAdapter {
    private List<Long> articleIds = new ArrayList<>();

    public ArticlesPagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
    }

    @Override
    public int getCount() {
        return articleIds.size();
    }

    @Override
    public ArticleItemFragment getItem(int position) {
        ArticleItemFragment f = new ArticleItemFragment();
        Long id = articleIds.get(position);
        f.setArticleId(id);
        return f;
    }

    public void updateIds(List<Long> newIds) {
        articleIds.clear();
        articleIds.addAll(newIds);
        notifyDataSetChanged();
    }

    public int getPositionForId(Long itemId) {
        return articleIds.indexOf(itemId);
    }

    @Nullable
    public Long getIdForPosition(int position) {
        return (position >= articleIds.size() || articleIds.size() == 0) ? null : articleIds.get(position);
    }

}
