package com.sorykhan.libroread

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.database.BookRoomDatabase
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DaoTest {
    private lateinit var bookDao: BookDao
    private lateinit var db: BookRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BookRoomDatabase::class.java).build()
        bookDao = db.bookDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getBookBySearch() {
        val book = Book(1, "Book numba 1", "C:\\Docs\\path\\example\\Book numba 1.pdf", 5700)
        bookDao.insertBook(book)
        val byName = bookDao.getBySearch("Book numba 1")
        assertEquals(byName.asLiveData().value, book)
    }

    @Test
    fun getAllBooks() {
        val books = listOf<Book>(
            Book(bookName = "Book 1", bookPath = "C:1", bookSize = 1),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 12),
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234)
        )
        books.forEach {
            bookDao.insertBook(it)
        }
        assertEquals(bookDao.getAll().asLiveData().value, books)
    }

    @Test
    fun getStartedBooks() {
        val books = listOf<Book>(
            Book(bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookProgress = 200),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookProgress = 1207),
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234)
        )

        books.forEach {
            bookDao.insertBook(it)
        }
        assertEquals(bookDao.getStartedBooks().asLiveData().value, books.subList(0, 2))
    }

    @Test
    fun getFavoriteBooks() {
        val books = listOf<Book>(
            Book(bookName = "Book 1", bookPath = "C:1", bookSize = 1),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookProgress = 1207),
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123, isFavorite = true),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234, isFavorite = true)
        )

        books.forEach {
            bookDao.insertBook(it)
        }
        assertEquals(bookDao.getStartedBooks().asLiveData().value, books.subList(2, 4))
    }
}