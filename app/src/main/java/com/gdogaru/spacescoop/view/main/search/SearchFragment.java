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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.databinding.MainSearchBinding;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.di.ViewModelFactory;
import com.gdogaru.spacescoop.view.main.ArticleDisplayer;
import com.gdogaru.spacescoop.view.main.HasTitle;
import com.gdogaru.spacescoop.view.main.scoops.list.ArticleListAdapter;

import java.util.Locale;

import javax.inject.Inject;


public class SearchFragment extends BaseFragment implements HasTitle {
    @Inject
    ImageDownloader imageDownloader;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    ArticleDisplayer articleDisplayer;
    @Inject
    AppSettingsController settingsController;

    private SearchViewModel viewmodel;
    private MainSearchBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = viewModelFactory.create(SearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MainSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();
    }

    private void setUI() {
        binding.searchText.setImeActionLabel(getString(R.string.search), KeyEvent.KEYCODE_ENTER);
        binding.searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.searchText.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                search();
                return true;
            }
            return false;
        });

        ArticleListAdapter mainListAdapter = new ArticleListAdapter(requireActivity(), settingsController.getLanguage(), imageDownloader, (newsId, image) -> articleDisplayer.displayArticle(null, newsId.getId()));
        binding.recycler.setAdapter(mainListAdapter);
        binding.recycler.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
        viewmodel.getListLiveData().observe(this, mainListAdapter::submitList);

        binding.searchIcon.setOnClickListener(v -> search());
    }

    public void search() {
        String query = String.valueOf(binding.searchText.getText()).toLowerCase(Locale.getDefault());
        viewmodel.setSearchTerm(query);

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.searchText.getWindowToken(), 0);
    }

    @Override
    public String getTitle() {
        return getString(R.string.search);
    }
}
