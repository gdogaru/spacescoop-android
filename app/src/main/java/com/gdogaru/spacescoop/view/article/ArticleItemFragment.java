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

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.evernote.android.state.State;
import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.databinding.ArticlesItemBinding;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.db.model.Article;
import com.gdogaru.spacescoop.events.PageChangedEvent;
import com.gdogaru.spacescoop.util.FormatUtil;
import com.gdogaru.spacescoop.util.HtmlHelper;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class ArticleItemFragment extends BaseFragment {
    @State
    Article article;
    @State
    Long articleId;
    boolean currentItem = false;

    @Inject
    ImageDownloader imageDownloader;
    @Inject
    NewsDao newsDao;

    private ArticlesItemBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ArticlesItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        Typeface tf = ResourcesCompat.getFont(activity, R.font.signika_regular);
        binding.collapsingToolbar.setCollapsedTitleTypeface(tf);
        binding.collapsingToolbar.setExpandedTitleTypeface(tf);

        binding.webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        ((WebView) binding.webView).getSettings().setJavaScriptEnabled(true);

        if (article == null) {
            newsDao.queryForId(articleId).observe(getViewLifecycleOwner(), result -> {
                article = result;
                displayArticle(article);
            });
        } else {
            displayArticle(article);
        }
    }

    private void displayArticle(Article article) {
        binding.toolbar.setTitle(article.getTitle());
        binding.toolbar.setContentDescription(article.getTitle());

        binding.newsImage.setOnClickListener(v -> FullImageActivity.start(requireActivity(), article.getHeadImageUrl(), article.getTitle(), binding.newsImage));
        binding.newsImage.setContentDescription(getString(R.string.article_image_description, article.getTitle()));

        binding.dateText.setText(FormatUtil.formatForUI(article.getPublishDate(), "en"));
        imageDownloader.display(article.getHeadImageUrl(), binding.newsImage);


        String data = HtmlHelper.asHtmlPage(article.getText(), isLandscapeTablet());
        ((WebView) binding.webView).loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);

        if (currentItem) {
            setActivityTitle();
        }
    }

    @Subscribe
    public void onPageChanged(PageChangedEvent e) {
        currentItem = e.getArticleId() == articleId;
        if (currentItem) {
            setActivityTitle();
            requireActivity().startPostponedEnterTransition();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, article.getLink());

            startActivity(Intent.createChooser(share, "Share scoop!"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setActivityTitle() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (article != null) binding.toolbar.setTitle(article.getTitle());
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
