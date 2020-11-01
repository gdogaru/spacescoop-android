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

package com.gdogaru.spacescoop.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.gdogaru.spacescoop.db.model.Article;
import com.gdogaru.spacescoop.db.model.ArticlePreview;
import com.gdogaru.spacescoop.db.model.ArticleThumb;

import java.util.Date;
import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT id, headImageUrl, title, publishDate, previewText, language FROM articles where language=:lang order by publishDate desc")
    DataSource.Factory<Integer, ArticlePreview> articlePreviews(String lang);

    @Query("SELECT id, headImageUrl, title, publishDate, previewText, language FROM articles where language=:lang and (title like :input or text like :input) order by publishDate desc")
    DataSource.Factory<Integer, ArticlePreview> newsSearchCursor(String input, String lang);

    @Query("SELECT id, title, headImageUrl, language FROM articles where language=:lang order by publishDate desc")
    DataSource.Factory<Integer, ArticleThumb> thumbsCursor(String lang);

    @Query("SELECT id, headImageUrl, title, publishDate, previewText, language FROM articles where language=:lang and title like :query  order by publishDate desc")
    DataSource.Factory<Integer, ArticlePreview> searchCursor(String query, String lang);

    @Query("SELECT id FROM articles where language=:lang order by publishDate desc")
    LiveData<List<Long>> getAllIds(String lang);

    @Query("SELECT id FROM articles where title like :query  order by publishDate desc")
    List<Long> getAllIdsForQuery(String query);

    @Query("select * from articles where id=:id")
    LiveData<Article> queryForId(Long id);

    @Query("select max(publishDate) from articles")
    Date getNewestDate();

    @Query("select min(publishDate) from articles")
    Date getOldestDate();

    @Insert
    void save(Article articleArticle);

    @Query("select count(*) from articles where guid=:guid")
    int countByGuId(String guid);

}
