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

package com.gdogaru.spacescoop.view.language;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.api.model.Language;

import java.util.List;

import timber.log.Timber;

/**
 *
 */
public class SelectLanguageMenuAsync extends AsyncTask<Void, Void, List<Language>> {
    private Context context;
    private ProgressDialog progressDialog;

    public SelectLanguageMenuAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage(context.getString(R.string.processing));
        progressDialog.show();
    }

    @Override
    protected List<Language> doInBackground(Void... params) {
        try {
            return null; //new DownloadLanguageListTask().getLanguagesFromServer();
        } catch (Exception e) {
            Timber.e(e, "getLanguagesFromServer");
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Language> languages) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (languages == null || languages.isEmpty()) {
            Toast.makeText(context, R.string.can_not_take_language, Toast.LENGTH_LONG).show();
        } else {
            showLanguageSelectDialog(languages);
        }
    }

    private String[] takeCompleteLangName(List<Language> languagesList) {
        String[] languages = new String[languagesList.size()];
        for (int i = 0; i < languagesList.size(); ++i) {
            languages[i] = languagesList.get(i).getCompleteName();
        }
        return languages;
    }

    private void showLanguageSelectDialog(final List<Language> languages) {
        String[] languagesComplete = takeCompleteLangName(languages);
        int idx = 0;
        final String[] selectedLanguage = {"en"};// {AppSettingsController.getLanguage(context)};
        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).getShortName().equals(selectedLanguage[0])) {
                idx = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_language)
                .setSingleChoiceItems(languagesComplete, idx, (dialog, which) -> selectedLanguage[0] = languages.get(which).getShortName())
                .setPositiveButton(R.string.ok, (dialog, which) -> beginChangeLanguage(selectedLanguage[0]))
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void beginChangeLanguage(String selectedLanguage) {
//        NewsFetcherSvc.changeLanguage(selectedLanguage, context);
    }
}