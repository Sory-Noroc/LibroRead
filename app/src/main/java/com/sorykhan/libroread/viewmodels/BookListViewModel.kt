package com.sorykhan.libroread.viewmodels

import androidx.lifecycle.*
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.utils.PdfUtils
import kotlinx.coroutines.launch
import java.io.File
import java.lang.IllegalArgumentException

class BookListViewModel(private val bookDao: BookDao): ViewModel() {

    private val utils = PdfUtils()

    // Keeping them as functions instead of variables to preserve space
    fun getAllBooks(): LiveData<List<Book>> = bookDao.getAll().asLiveData()
    fun getStartedBooks() = bookDao.getStartedBooks().asLiveData()
    fun getFavoriteBooks() = bookDao.getFavoriteBooks().asLiveData()
    fun getBooksBySearch(search: String) = bookDao.getBySearch(search).asLiveData()

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

class BookListViewModelFactory(private val bookDao: BookDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookListViewModel(bookDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}