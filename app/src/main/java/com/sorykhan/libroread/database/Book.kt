package com.sorykhan.libroread.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull @ColumnInfo(name = "book_name") val bookName: String,
    @NonNull @ColumnInfo(name = "path") val bookPath: String,
    @NonNull @ColumnInfo(name = "size") val bookSize: Long,
    @NonNull @ColumnInfo(name = "progress") val bookProgress: Int = 0,
    @NonNull @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
    )