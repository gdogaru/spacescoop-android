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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AnalyticsHelper;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.view.common.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public class FullImageActivity extends BaseActivity {

    public static final String IMAGE_URI = "image_uri";
    public static final String IMAGE_TITLE = "image_title";

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ImageDownloader imageDownloader;
    @Inject
    AnalyticsHelper analyticsHelper;

    public static void start(Activity activity, String url, String title, ImageView fromView) {
        Intent intent = new Intent(activity, FullImageActivity.class);
        intent.putExtra(IMAGE_URI, url);
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, fromView, activity.getString(R.string.article_image_transition));
        ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_full_image);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageDownloader.display(getIntent().getStringExtra(IMAGE_URI), imageView);
        imageView.setContentDescription(getString(R.string.article_image_description, getIntent().getStringExtra(IMAGE_TITLE)));
        analyticsHelper.logEvent(AnalyticsHelper.Event.FULL_IMAGE_VIEW, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
