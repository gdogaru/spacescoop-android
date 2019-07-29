package com.gdogaru.spacescoop.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleThumb implements HasId {
    @PrimaryKey
    @ColumnInfo(name = "id")
    long id;

    @ColumnInfo(name = "headImageUrl")
    String headImageUrl;

    @ColumnInfo(name = "language")
    String language;
}
