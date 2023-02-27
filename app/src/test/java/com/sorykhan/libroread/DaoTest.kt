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

@RunWith(AndroidJUnit4::class)
class DaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var bookDao: BookDao
    private lateinit var db: BookRoomDatabase
    private lateinit var books: List<Book>

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BookRoomDatabase::class.java).allowMainThreadQueries().build()
        bookDao = db.bookDao()

        books = listOf(
            Book(bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookPages = 45, bookProgress = 22),
            Book(bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookPages = 44, bookProgress = 1207),
            Book(bookName = "Book 3", bookPath = "C:3", bookSize = 123, bookPages = 78, isFavorite = true),
            Book(bookName = "Book 4", bookPath = "C:4", bookSize = 1234, bookPages = 42, isFavorite = true)
        )

        books.forEach {
            runBlocking {
                bookDao.insertBook(it)
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getBooksBySearch() {
        val requestedBook = bookDao.getBySearch("Book 1")
        val expectedBook = Book(id = 1, bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookPages = 45, bookProgress = 22)
        assertEquals(expectedBook, requestedBook)
    }

    @Test
    fun getAllBooks() {
        val expectedBooks = listOf(
            Book(id = 1, bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookPages = 45, bookProgress = 22),
            Book(id = 2, bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookPages = 44, bookProgress = 1207),
            Book(id = 3, bookName = "Book 3", bookPath = "C:3", bookSize = 123, bookPages = 78, isFavorite = true),
            Book(id = 4, bookName = "Book 4", bookPath = "C:4", bookSize = 1234, bookPages = 42, isFavorite = true)
        )

        val allBooks = bookDao.getAll().asLiveData().getOrAwaitValue()
        assertEquals(expectedBooks, allBooks)
    }

    @Test
    fun getStartedBooks() {
        val expectedBooks = listOf(
            Book(id = 1, bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookProgress = 22, bookPages = 45),
            Book(id = 2, bookName = "Book 2", bookPath = "C:2", bookSize = 12, bookProgress = 1207, bookPages = 44)
        )
        val startedBooks = bookDao.getStartedBooks().asLiveData().getOrAwaitValue()
        assertEquals(expectedBooks, startedBooks)
    }

    @Test
    fun getFavoriteBooks() {
        val expectedBooks = listOf(
            Book(id = 3, bookName = "Book 3", bookPath = "C:3", bookSize = 123, isFavorite = true, bookPages = 78),
            Book(id = 4, bookName = "Book 4", bookPath = "C:4", bookSize = 1234, isFavorite = true, bookPages = 42)
        )

        val favoriteBooks = bookDao.getFavoriteBooks().asLiveData().getOrAwaitValue()
        assertEquals(expectedBooks, favoriteBooks)
    }

    @Test
    fun getFavoriteBookStatus() {

        // Reverse order
        var isFavoriteStatus: Boolean = bookDao.getBookFavoriteStatus("C:4")
        assertEquals(true, isFavoriteStatus)
        isFavoriteStatus = bookDao.getBookFavoriteStatus("C:3")
        assertEquals(true, isFavoriteStatus)
        isFavoriteStatus = bookDao.getBookFavoriteStatus("C:2")
        assertEquals(false, isFavoriteStatus)
        isFavoriteStatus = bookDao.getBookFavoriteStatus("C:1")
        assertEquals(false, isFavoriteStatus)
    }

    @Test
    fun getBySearch() {
        var bookFound = bookDao.getBySearch("Book 2")
        assertEquals("C:2", bookFound?.bookPath)
        bookFound = bookDao.getBySearch("Some random wrong name")
        assertEquals(null, bookFound)
    }

    @Test
    fun updateProgress() {
        val expectedBook = Book(id = 1, bookName = "Book 1", bookPath = "C:1", bookSize = 1, bookPages = 45, bookProgress = 23)
        runBlocking {
            bookDao.updateProgress("C:1", 23)
        }
        val actualBook = bookDao.getBySearch("Book 1")
        assertEquals(actualBook, expectedBook)
    }
}
