package com.sorykhan.libroread.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "book_name") val bookName: String,
    @ColumnInfo(name = "path") val bookPath: String,
    @ColumnInfo(name = "pages") val bookPages: Int,
    @ColumnInfo(name = "size") val bookSize: Long,
    @ColumnInfo(name = "progress") val bookProgress: Int = 0,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
    )