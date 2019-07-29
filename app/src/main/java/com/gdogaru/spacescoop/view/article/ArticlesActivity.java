package com.gdogaru.spacescoop.view.article;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.events.PageChangedEvent;
import com.gdogaru.spacescoop.view.common.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gabriel on 7/21/2015.
 */
public class ArticlesActivity extends BaseActivity {
    public static final String ARG_ARTICLE_ID = "ARTICLE_ID";
    private static final String PAGER_KEY = "PAGER";

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @Inject
    NewsDao newsDao;
    @Inject
    AppSettingsController settingsController;

    private ArticlesPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(isLandscapeTablet() ? R.style.SpaceScoopTheme_Light : R.style.SpaceScoopTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.articles);
        ButterKnife.bind(this);

        initViewPager();
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
        outState.putParcelable(PAGER_KEY, viewPager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(PAGER_KEY)) {
            viewPager.onRestoreInstanceState(savedInstanceState.getParcelable(PAGER_KEY));
        }
    }

    private void initViewPager() {
        adapter = new ArticlesPagerAdapter(this);
        viewPager.setAdapter(adapter);
        newsDao.getAllIds(settingsController.getLanguage()).observe(this, longs -> {
            if (longs.size() > 0) {
                boolean first = adapter.getCount() == 0;
                adapter.updateIds(longs);
                postSelectedPage(viewPager.getCurrentItem());
                if (first) setItemId();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                postSelectedPage(position);
            }
        });
    }

    private void postSelectedPage(int position) {
        Long id = adapter.getIdForPosition(position);
        if (id != null)
            viewPager.postDelayed(() -> bus.post(new PageChangedEvent(id, position)), 100);
    }


    public void setItemId() {
        long itemId = getIntent().getLongExtra(ARG_ARTICLE_ID, -1);
        if (itemId > 0) {
            if (viewPager != null && adapter != null) {
                viewPager.setCurrentItem(adapter.getPositionForId(itemId));
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
