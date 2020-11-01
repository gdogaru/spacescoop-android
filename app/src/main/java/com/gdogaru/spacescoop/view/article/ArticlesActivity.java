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

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.databinding.ArticlesBinding;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.events.PageChangedEvent;
import com.gdogaru.spacescoop.view.common.BaseActivity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public class ArticlesActivity extends BaseActivity {
    public static final String ARG_ARTICLE_ID = "ARTICLE_ID";
    private static final String PAGER_KEY = "PAGER";

    @Inject
    NewsDao newsDao;
    @Inject
    AppSettingsController settingsController;

    private ArticlesPagerAdapter adapter;
    private ArticlesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(isLandscapeTablet() ? R.style.SpaceScoopTheme_Light : R.style.SpaceScoopTheme);
        super.onCreate(savedInstanceState);
        postponeEnterTransition();

        binding = ArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPagerFitFlags();
        initViewPager();
    }

    private void setupViewPagerFitFlags() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.viewPager, (v, insets) -> {
            insets = ViewCompat.onApplyWindowInsets(v, insets);
            if (insets.isConsumed()) {
                return insets;
            }

            boolean consumed = false;
            for (int i = 0, count = binding.viewPager.getChildCount(); i < count; ++i) {
                ViewCompat.dispatchApplyWindowInsets(binding.viewPager.getChildAt(i), insets);
                if (insets.isConsumed()) {
                    consumed = true;
                }
            }
            return consumed ? insets.consumeSystemWindowInsets() : insets;
        });

        binding.viewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PAGER_KEY, binding.viewPager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(PAGER_KEY)) {
            binding.viewPager.onRestoreInstanceState(savedInstanceState.getParcelable(PAGER_KEY));
        }
    }

    private void initViewPager() {
        adapter = new ArticlesPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        newsDao.getAllIds(settingsController.getLanguage()).observe(this, longs -> {
            if (longs.size() > 0) {
                boolean first = adapter.getCount() == 0;
                adapter.updateIds(longs);
                if (first) setItemId();
                postSelectedPage(binding.viewPager.getCurrentItem());
            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                postSelectedPage(position);
            }
        });
    }

    private void postSelectedPage(int position) {
        Long id = adapter.getIdForPosition(position);
        if (id != null) {
            binding.viewPager.postDelayed(() -> bus.post(new PageChangedEvent(id, position)), 100);
        }
    }


    public void setItemId() {
        long itemId = getIntent().getLongExtra(ARG_ARTICLE_ID, -1);
        if (itemId > -1) {
            if (binding.viewPager != null && adapter != null) {
                binding.viewPager.setCurrentItem(adapter.getPositionForId(itemId), false);
            }
        }
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        if (isLandscapeTablet()) {
            theme.applyStyle(R.style.SpaceScoopTheme_Light, true);
        }
        return theme;
    }

}
