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

package com.gdogaru.spacescoop.view.main.scoops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.databinding.MainScoopsBinding;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.di.ViewModelFactory;
import com.gdogaru.spacescoop.view.main.ArticleDisplayer;
import com.gdogaru.spacescoop.view.main.HasTitle;
import com.gdogaru.spacescoop.view.main.scoops.grid.ArticleGridAdapter;
import com.gdogaru.spacescoop.view.main.scoops.list.ArticleListAdapter;

import javax.inject.Inject;

public class ScoopsFragment extends BaseFragment implements HasTitle {

    public static final int GRID_COLUMNS = 3;

    @Inject
    ImageDownloader imageDownloader;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    AppSettingsController settingsController;
    @Inject
    ArticleDisplayer articleDisplayer;

    private ScoopsViewModel viewModel;
    private ArticleListAdapter mainListAdapter;
    private ArticleGridAdapter mainGridAdapter;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager listLayoutManager;
    private RecyclerView.LayoutManager layoutManager;
    private MainScoopsBinding binding;

    @Override
    public String getTitle() {
        return getString(R.string.unawe_title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = viewModelFactory.create(ScoopsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MainScoopsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsController.isInListModeLiveData().observe(getViewLifecycleOwner(), this::changeLayout);

        binding.swipeRefreshLayout.setOnRefreshListener(() ->
                viewModel.getNewScoops().observe(getViewLifecycleOwner(), staleState -> {
                    if (staleState.isEndState()) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }));

        String lang = settingsController.getLanguage();
        FragmentActivity activity = requireActivity();
        mainListAdapter = new ArticleListAdapter(activity, lang, imageDownloader, (newsId, image) -> articleDisplayer.displayArticle(image, newsId.getId()));
        mainGridAdapter = new ArticleGridAdapter(activity, lang, imageDownloader, (newsId, image) -> articleDisplayer.displayArticle(image, newsId.getId()));
        viewModel.getListLiveData().observe(getViewLifecycleOwner(), newsPreviews -> mainListAdapter.submitList(newsPreviews));
        viewModel.getGridLiveData().observe(getViewLifecycleOwner(), newsPreviews -> mainGridAdapter.submitList(newsPreviews));
        gridLayoutManager = new GridLayoutManager(activity, getResources().getInteger(R.integer.scoops_grid_columns));
        listLayoutManager = new GridLayoutManager(activity, getResources().getInteger(R.integer.scoops_list_columns));

        viewModel.loadMoreActive().observe(getViewLifecycleOwner(), loading -> binding.bottomProgress.setVisibility(loading ? View.VISIBLE : View.GONE));
        initScrollLoader();
    }

    private void changeLayout(boolean isList) {
        RecyclerView.Adapter<?> adapter;
        if (isList) {
            adapter = mainListAdapter;
            layoutManager = listLayoutManager;
        } else {
            adapter = mainGridAdapter;
            layoutManager = gridLayoutManager;
        }
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(layoutManager);
    }

    private void initScrollLoader() {
        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager != null) {
                    GridLayoutManager gm = (GridLayoutManager) ScoopsFragment.this.layoutManager;
                    int lastPos = gm.findLastCompletelyVisibleItemPosition();
                    int items = mainListAdapter.getItemCount();
                    int limit = 2 * gm.getSpanCount();
                    Boolean value = viewModel.loadMoreActive().getValue();
                    if (lastPos > items - limit && (value == null || !value)) {
                        viewModel.loadMore();
                    }
                }
            }
        });
    }
}
