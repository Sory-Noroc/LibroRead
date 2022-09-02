package com.sorykhan.libroread.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.utils.PdfUtils
import kotlinx.coroutines.launch
import java.io.File

class BookListViewModel(private val bookDao: BookDao): ViewModel() {

    // Keeping them as functions instead of variables to preserve space
    fun getAllBooks(): LiveData<List<Book>> = bookDao.getAll().asLiveData()
    fun getStartedBooks() = bookDao.getStartedBooks().asLiveData()
    fun getFavoriteBooks() = bookDao.getFavoriteBooks().asLiveData()
    fun getBooksBySearch(search: String) = bookDao.getBySearch(search).asLiveData()

    var currentList = getAllBooks()

    fun insertBook(name: String, path: String) {  // Should be called before the UI is even built
        val size = File(path).length()
        viewModelScope.launch {
            bookDao.insertBook(Book(bookName=name, bookPath=path, bookSize=size))
        }
    }

    fun updateProgress(path: String, progress: Double) {
        viewModelScope.launch {
            bookDao.updateProgress(path, progress)
        }
    }

    fun updateIsFavorite(path: String, isFavorite: Boolean) {
        viewModelScope.launch {
            bookDao.updateIsFavorite(path, isFavorite)
        }
    }
}