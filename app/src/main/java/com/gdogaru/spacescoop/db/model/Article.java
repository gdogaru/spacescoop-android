package com.gdogaru.spacescoop.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "articles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    long id;

    @ColumnInfo(name = "url")
    String url;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "link")
    String link;

    @ColumnInfo(name = "text")
    String text;

    @ColumnInfo(name = "publishDate")
    Date publishDate;

    @ColumnInfo(name = "guid")
    String guid;

    @ColumnInfo(name = "headImageUrl")
    String headImageUrl;

    @ColumnInfo(name = "previewText")
    String previewText;

    @ColumnInfo(name = "language")
    String language;
}
