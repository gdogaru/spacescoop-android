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

package com.gdogaru.spacescoop.view.main.search;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.di.ViewModelFactory;
import com.gdogaru.spacescoop.view.main.ArticleDisplayer;
import com.gdogaru.spacescoop.view.main.HasTitle;
import com.gdogaru.spacescoop.view.main.scoops.list.ArticleListAdapter;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchFragment extends BaseFragment implements HasTitle {
    @Inject
    ImageDownloader imageDownloader;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    ArticleDisplayer articleDisplayer;
    @Inject
    AppSettingsController settingsController;

    @BindView(R.id.search_icon)
    ImageButton searchButton;
    @BindView(R.id.search_text)
    EditText searchText;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private SearchViewModel viewmodel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = viewModelFactory.create(SearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUI();
    }

    private void setUI() {
        searchText.setImeActionLabel(getString(R.string.search), KeyEvent.KEYCODE_ENTER);
        searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchText.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                search();
                return true;
            }
            return false;
        });

        ArticleListAdapter mainListAdapter = new ArticleListAdapter(requireActivity(), settingsController.getLanguage(), imageDownloader, (newsId, image) -> articleDisplayer.displayArticle(null, newsId.getId()));
        recycler.setAdapter(mainListAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        viewmodel.getListLiveData().observe(this, mainListAdapter::submitList);
    }

    @OnClick(R.id.search_icon)
    public void search() {
        String query = String.valueOf(searchText.getText()).toLowerCase(Locale.getDefault());
        viewmodel.setSearchTerm(query);

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }

    @Override
    public String getTitle() {
        return getString(R.string.search);
    }
}
