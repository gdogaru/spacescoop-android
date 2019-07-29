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
