package com.gdogaru.spacescoop.view.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AnalyticsHelper;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.view.common.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gabriel on 28.08.2016.
 */
public class FullImageActivity extends BaseActivity {

    public static final String IMAGE_URI = "image_uri";

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ImageDownloader imageDownloader;
    @Inject
    AnalyticsHelper analyticsHelper;

    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, FullImageActivity.class);
        intent.putExtra(IMAGE_URI, url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.full_image);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageDownloader.display(getIntent().getStringExtra(IMAGE_URI), imageView);
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
