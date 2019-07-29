package com.gdogaru.spacescoop.view.main.scoops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.di.ViewModelFactory;
import com.gdogaru.spacescoop.view.main.ArticleDisplayer;
import com.gdogaru.spacescoop.view.main.HasTitle;
import com.gdogaru.spacescoop.view.main.scoops.grid.ArticleGridAdapter;
import com.gdogaru.spacescoop.view.main.scoops.list.ArticleListAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoopsFragment extends BaseFragment implements HasTitle {

    public static final int GRID_COLUMNS = 3;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.bottom_progress)
    ProgressBar bottomProgress;

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
        return inflater.inflate(R.layout.main_scoops, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        settingsController.isInListModeLiveData().observe(this, this::changeLayout);

        swipeRefreshLayout.setOnRefreshListener(() ->
                viewModel.getNewScoops().observe(ScoopsFragment.this, staleState -> {
                    if (staleState.isEndState()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }));

        String lang = settingsController.getLanguage();
        FragmentActivity activity = requireActivity();
        mainListAdapter = new ArticleListAdapter(activity, lang, imageDownloader, (newsId, image) -> articleDisplayer.displayArticle(image, newsId.getId()));
        mainGridAdapter = new ArticleGridAdapter(activity, lang, imageDownloader, (newsId, image) -> articleDisplayer.displayArticle(image, newsId.getId()));
        viewModel.getListLiveData().observe(this, newsPreviews -> mainListAdapter.submitList(newsPreviews));
        viewModel.getGridLiveData().observe(this, newsPreviews -> mainGridAdapter.submitList(newsPreviews));
        gridLayoutManager = new GridLayoutManager(activity, getResources().getInteger(R.integer.scoops_grid_columns));
        listLayoutManager = new GridLayoutManager(activity, getResources().getInteger(R.integer.scoops_list_columns));

        viewModel.loadMoreActive().observe(this, loading -> bottomProgress.setVisibility(loading ? View.VISIBLE : View.GONE));
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
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);
    }

    private void initScrollLoader() {
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
