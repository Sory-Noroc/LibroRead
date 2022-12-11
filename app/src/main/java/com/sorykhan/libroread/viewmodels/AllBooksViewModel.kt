package com.sorykhan.libroread.viewmodels

import androidx.lifecycle.*
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.utils.PdfUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AllBooksViewModel(private val bookDao: BookDao): ViewModel() {

    // Keeping them as functions instead of variables to preserve space
    fun getAllBooks() = bookDao.getAll()
//    fun getStartedBooks() = bookDao.getStartedBooks()
//    fun getFavoriteBooks() = bookDao.getFavoriteBooks()
//    fun getBooksBySearch(search: String) = bookDao.getBySearch(search)
//    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)

    fun insertBook(name: String, path: String) {  // Should be called before the UI is even built
        val file = File(path)
        viewModelScope.launch {
            bookDao.insertBook(Book(
                bookName=name,
                bookPath=path,
                bookPages = PdfUtils.getPdfPageCount(file),
                bookSize=file.length()
            ))
        }
    }

    fun updateProgress(path: String, progress: Int) {
        viewModelScope.launch {
            bookDao.updateProgress(path, progress)
        }
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