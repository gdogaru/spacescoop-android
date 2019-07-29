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

    @Query("SELECT id, headImageUrl, language FROM articles where language=:lang order by publishDate desc")
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
