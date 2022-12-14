package com.sorykhan.libroread.viewmodels

import android.os.Environment
import android.util.Log
import androidx.lifecycle.*
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.utils.PdfUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "AllBooksViewModel"

class AllBooksViewModel(private val bookDao: BookDao): ViewModel() {

    fun uploadToDatabase() {
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        Log.i(TAG, "Downloads path: $downloadsPath")
        for (pdf in PdfUtils.getPDFs(downloadsPath)) {
            viewModelScope.launch(Dispatchers.IO) {
                val bookInDbName = bookDao.getBySearch(pdf.name)
                if (bookInDbName == null) {
                    Log.i(TAG, "Book not already in DB, have to insert")
                    insertBook(pdf)
                }
            }
        }
    }

    // Keeping them as functions instead of variables to preserve space
    fun getAllBooks() = bookDao.getAll()
//    fun getStartedBooks() = bookDao.getStartedBooks()
//    fun getFavoriteBooks() = bookDao.getFavoriteBooks()
//    fun getBooksBySearch(search: String) = bookDao.getBySearch(search)
//    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)

    private suspend fun insertBook(file: File) {  // Should be called before the UI is even built
        val (name, path, size) = PdfUtils.getPdfInfo(file)
        val pageCount: Int = PdfUtils.getPdfPageCount(file) ?: 0
        val book = Book(bookName=name, bookPath=path, bookPages = pageCount, bookSize=size)
        bookDao.insertBook(book)
        Log.d(TAG, "Book: $book")
    }

    fun updateIsFavorite(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = bookDao.getBookFavoriteStatus(path)
            bookDao.updateIsFavorite(path, !isFavorite)
        }
    }
}

class BookListViewModelFactory(private val bookDao: BookDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllBooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllBooksViewModel(bookDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}