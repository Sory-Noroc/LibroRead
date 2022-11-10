package com.sorykhan.libroread

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.database.BookRoomDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.math.exp

@RunWith(AndroidJUnit4::class)
class DaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var bookDao: BookDao
    private lateinit var db: BookRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BookRoomDatabase::class.java).allowMainThreadQueries().build()
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
        val book = Book(bookName="Book numba 1", bookPath="C:\\Docs\\path\\example\\Book numba 1.pdf", bookSize=5700, bookPages=10)
        runBlocking {
            bookDao.insertBook(book)
        }
        val expectedBook = Book(id = 1, bookName="Book numba 1", bookPath="C:\\Docs\\path\\example\\Book numba 1.pdf", bookSize=5700, bookPages=10)

        val requestedBook = bookDao.getBySearch("Book numba 1").asLiveData().getOrAwaitValue()
        assertEquals(expectedBook, requestedBook)
//        db.clearAllTables()
    }

    @Test
    fun getBooksBySearch() {
        val books = listOf(
            Book(bookName="Book numba 1", bookPath="C:\\Docs\\path\\example\\Book numba 1.pdf", bookSize=5700, bookPages=10),
            Book(bookName="Book numba 1", bookPath="C:\\Docs\\path\\example\\Book numba 1.pdf", bookSize=5700, bookPages=10)
        )
        runBlocking {
            books.forEach {
                bookDao.insertBook(it)
            }
        }
        val expectedBook = Book(id = 1, bookName="Book numba 1", bookPath="C:\\Docs\\path\\example\\Book numba 1.pdf", bookSize=5700, bookPages=10)

        val requestedBook = bookDao.getBySearch("Book numba 1").asLiveData().getOrAwaitValue()
        assertEquals(expectedBook, requestedBook)
    }

    @Test
    fun getAllBooks() {
        val books = listOf(
            Book(bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookPages = 55),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookPages = 11),
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123, bookPages = 94),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234, bookPages = 349)
        )

        books.forEach {
            runBlocking {
                bookDao.insertBook(it)
            }
        }

        val expectedBooks = listOf(
            Book(id = 1, bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookPages = 55),
            Book(id = 2, bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookPages = 11),
            Book(id = 3, bookName = "Book 3", bookPath = "C:3", bookSize = 123, bookPages = 94),
            Book(id = 4, bookName = "Book 4", bookPath = "C:4", bookSize = 1234, bookPages = 349)
        )

        val allBooks = bookDao.getAll().asLiveData().getOrAwaitValue()
        assertEquals(expectedBooks, allBooks)
    }

    @Test
    fun getStartedBooks() {
        val books = listOf(
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123, bookPages = 22),
            Book(bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookProgress = 200, bookPages = 20),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookProgress = 1207, bookPages = 12),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234, bookPages = 67)
        )

        books.forEach {
            runBlocking {
                bookDao.insertBook(it)
            }
        }
        val expectedBooks = listOf(
            Book(id = 2, bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookProgress = 200, bookPages = 20),
            Book(id = 3, bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookProgress = 1207, bookPages = 12)
        )
        val startedBooks = bookDao.getStartedBooks().asLiveData().getOrAwaitValue()
        assertEquals(expectedBooks, startedBooks)
    }

    @Test
    fun getFavoriteBooks() {
        val books = listOf(
            Book(bookName = "Book 1", bookPath = "C:1", bookPages = 44, bookSize = 12, bookProgress = 1207),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 1, bookPages = 45),
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123, bookPages = 78, isFavorite = true),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234, bookPages = 42, isFavorite = true)
        )

        books.forEach {
            runBlocking {
                bookDao.insertBook(it)
            }
        }
        val expectedBooks = listOf(
            Book(id = 3, bookName = "Book 3", bookPath = "C:3", bookSize = 123, isFavorite = true, bookPages = 78),
            Book(id = 4, bookName = "Book 4", bookPath = "C:4", bookSize = 1234, isFavorite = true, bookPages = 42)
        )

        val favoriteBooks = bookDao.getStartedBooks().asLiveData().getOrAwaitValue()
        assertEquals(expectedBooks, favoriteBooks)
    }
}