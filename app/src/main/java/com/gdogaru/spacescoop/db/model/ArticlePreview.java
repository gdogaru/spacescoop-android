package com.gdogaru.spacescoop.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreview implements HasId {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "headImageUrl")
    private String headImageUrl;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "publishDate")
    private Date publishDate;

    @ColumnInfo(name = "previewText")
    private String previewText;

    @ColumnInfo(name = "language")
    String language;
}
