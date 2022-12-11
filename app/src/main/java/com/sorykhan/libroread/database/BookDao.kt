package com.sorykhan.libroread.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
 @Query("SELECT * FROM Book ORDER BY book_name")
 fun getAll(): Flow<List<Book>>

 @Query("SELECT * FROM Book WHERE progress > 0 ORDER BY book_name")
 fun getStartedBooks(): Flow<List<Book>>

 @Query("SELECT * FROM Book WHERE is_favorite = 1 ORDER BY book_name")
 fun getFavoriteBooks(): Flow<List<Book>>

 @Query("SELECT is_favorite FROM Book WHERE path = :path")
 fun getBookFavoriteStatus(path: String): Boolean

 @Query("SELECT * FROM Book WHERE book_name = :bookName LIMIT 1")
 fun getBySearch(bookName: String): Book?

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertBook(book: Book)

 @Delete
 suspend fun deleteBook(book: Book)

 @Query("UPDATE Book SET progress = :progress WHERE path = :path")
 suspend fun updateProgress(path: String, progress: Int)

 @Query("UPDATE Book SET is_favorite = :is_favorite WHERE path = :path")
 suspend fun updateIsFavorite(path: String, is_favorite: Boolean)
}