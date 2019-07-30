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

package com.gdogaru.spacescoop.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gdogaru.spacescoop.api.model.Language;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

@Singleton
public class SpacescoopClient {
    private final SpacescoopService retrofit;

    @Inject
    public SpacescoopClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://www.unawe.org/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(SpacescoopService.class);
    }


    public LiveData<ApiResponse<List<Language>>> getLanguages() {
        MutableLiveData<ApiResponse<List<Language>>> result = new MutableLiveData<>();
        retrofit.getLanguages().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                result.setValue(new ApiResponse<>(ApiResponse.Status.SUCCESS, convertStringJsonToList(response.body())));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(new ApiResponse<>(ApiResponse.Status.ERROR, null));
            }
        });
        return result;
    }

    @SneakyThrows
    public ApiResponse<List<SyndEntry>> getRssItemsSync(String lang, int page) {
        Response<String> response = retrofit.getFeedPage(lang, page).execute();
        if (response.isSuccessful()) {
            return new ApiResponse<>(ApiResponse.Status.SUCCESS, convertToRss(response.body()));
        } else {
            return new ApiResponse<>(ApiResponse.Status.ERROR, null);
        }
    }

    public LiveData<ApiResponse<List<SyndEntry>>> getRssItems(String lang, int page) {
        MutableLiveData<ApiResponse<List<SyndEntry>>> result = new MutableLiveData<>();
        retrofit.getFeedPage(lang, page).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                result.setValue(new ApiResponse<>(ApiResponse.Status.SUCCESS, convertToRss(response.body())));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(new ApiResponse<>(ApiResponse.Status.ERROR, null));
            }
        });
        return result;
    }


    @SneakyThrows
    private List<SyndEntry> convertToRss(String body) {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(IOUtils.toInputStream(body)));
        return feed.getEntries();
    }


    private List<Language> convertStringJsonToList(String input) {
        List<Language> languages = new ArrayList<>();
        try {
            JSONArray jObject = new JSONArray(input);
            for (int i = 0; i < jObject.length(); i++) {
                try {
                    if (jObject.get(i) == null || "null".equals(jObject.get(i).toString()))
                        continue;

                    JSONArray langArray = jObject.getJSONArray(i);
                    Language language = new Language();
                    language.setShortName((String) langArray.get(0));
                    language.setCompleteName((String) langArray.get(1));
                    languages.add(language);
                } catch (Exception e) {
                    Timber.e(e, "Error parsing single country");
                }
            }
        } catch (JSONException exception) {
            Timber.e(exception, "cannot parse %s ", input);
        }
        return languages;
    }

}
