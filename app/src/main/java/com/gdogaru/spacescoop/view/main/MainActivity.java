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

package com.gdogaru.spacescoop.view.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.api.model.Language;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.controllers.RatingController;
import com.gdogaru.spacescoop.view.SplashActivity;
import com.gdogaru.spacescoop.view.article.ArticlesActivity;
import com.gdogaru.spacescoop.view.common.BaseActivity;
import com.gdogaru.spacescoop.view.common.GalaxyProgressDialog;
import com.gdogaru.spacescoop.view.di.ViewModelFactory;
import com.gdogaru.spacescoop.view.main.credits.CreditsFragment;
import com.gdogaru.spacescoop.view.main.scoops.ScoopsFragment;
import com.gdogaru.spacescoop.view.main.search.SearchFragment;
import com.gdogaru.spacescoop.view.main.settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ArticleDisplayer, NavigationView.OnNavigationItemSelectedListener {
    @Inject
    AppSettingsController settingsController;
    @Inject
    RatingController ratingHelper;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    ImageDownloader imageDownloader;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigationView;


    public long itemId;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem viewTypeToggle;
    private MainActivityViewModel viewmodel;
    private int startTransaction = -1;

    public static void start(AppCompatActivity activity, boolean clean) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (clean) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.act_fade_in, R.anim.act_fade_out);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        ButterKnife.bind(this);

        viewmodel = viewModelFactory.create(MainActivityViewModel.class);
        viewmodel.init();

        if (getIntent().hasExtra("item_id")) {
            itemId = getIntent().getLongExtra("itemId", 0);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.unawe_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(this);
        openDrawerFirstTime();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.central_view);
            if (f instanceof HasTitle) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) actionBar.setTitle(((HasTitle) f).getTitle());
            }
            if (viewTypeToggle != null) {
                viewTypeToggle.setVisible(f instanceof ScoopsFragment);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.central_view, new ScoopsFragment())
                .commit();
    }

    private void openDrawerFirstTime() {
        if (!settingsController.seenDrawer()) {
            drawerLayout.openDrawer(Gravity.LEFT);
            settingsController.setSeenDrawer(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (handleMenuItemId(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null) drawerToggle.syncState();
    }

    @Override
    public void displayArticle(@Nullable View fromView, long articleId) {
        Intent intent = new Intent(this, ArticlesActivity.class);
        intent.putExtra(ArticlesActivity.ARG_ARTICLE_ID, articleId);

        if (fromView != null) {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, fromView, getString(R.string.article_image_transition));
            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        viewTypeToggle = menu.findItem(R.id.action_view_type);
        settingsController.isInListModeLiveData().observe(this, isList -> viewTypeToggle.setIcon(isList ? R.drawable.button_list_view : R.drawable.button_grid_view));
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        handleMenuItemId(menuItem.getItemId());
        return false;
    }

    private boolean handleMenuItemId(int menuItemId) {
        switch (menuItemId) {
            case R.id.action_view_type:
                settingsController.setIsInListMode(!settingsController.isInListMode());
                closeDrawer();
                break;
            case R.id.scoops:
                revertToStackTop();
                closeDrawer();
                break;
            case R.id.search:
                showMainFragment(new SearchFragment(), true);
                break;
            case R.id.credits:
                showMainFragment(new CreditsFragment(), true);
                break;
            case R.id.language:
                showSelectLanguage();
                break;
            case R.id.rate:
                ratingHelper.goToPlay(this);
                break;
            case R.id.settings:
                showMainFragment(new SettingsFragment(), true);
                break;
            default:
                return false;
        }
        return true;
    }

    private void revertToStackTop() {
        if (startTransaction >= 0) {
            getSupportFragmentManager().popBackStack(startTransaction, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            startTransaction = -1;
        }
    }

    private void showSelectLanguage() {
        GalaxyProgressDialog loadingDialog = new GalaxyProgressDialog(this);
        loadingDialog.show();

        viewmodel.getLanguages().observe(this, listApiResponse -> {
            loadingDialog.cancel();
            showSelectLanguageDialog(listApiResponse.getValue());
        });
    }

    private void showSelectLanguageDialog(List<Language> languageList) {
        String[] languagesComplete = new String[languageList.size()];
        AtomicInteger selectedIdx = new AtomicInteger();
        String lang = settingsController.getLanguage();
        for (int i = 0; i < languageList.size(); i++) {
            languagesComplete[i] = languageList.get(i).getCompleteName();
            if (lang.equals(languageList.get(i).getShortName())) selectedIdx.set(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_language)
                .setSingleChoiceItems(languagesComplete, selectedIdx.get(), (dialog, which) -> selectedIdx.set(which))
                .setPositiveButton(R.string.change, (dialog, which) -> {
                    viewmodel.changeLanguage(languageList.get(selectedIdx.get()));
                    SplashActivity.start(this, true);
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
    }

    public void showMainFragment(Fragment fragment, boolean addToBackstack) {
        revertToStackTop();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.central_view, fragment);
        if (addToBackstack) fragmentTransaction.addToBackStack(fragment.getClass().getName());
        startTransaction = fragmentTransaction.commit();
        closeDrawer();
    }

}