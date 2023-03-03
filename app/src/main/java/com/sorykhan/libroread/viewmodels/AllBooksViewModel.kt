package com.sorykhan.libroread.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.lifecycle.*
import com.artifex.mupdf.fitz.Document
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.database.BookDao
import com.sorykhan.libroread.utils.PdfUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException

private const val TAG = "AllBooksViewModel"

class AllBooksViewModel(private val bookDao: BookDao): ViewModel() {

    var coverMap = MutableLiveData<Map<String, Bitmap?>>()

    fun uploadToDatabase() {
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        Log.i(TAG, "Downloads path: $downloadsPath")
        for (pdf in PdfUtils.getPDFs(downloadsPath)) {
            Log.d(TAG, "Checking pdf $pdf")
            viewModelScope.launch(Dispatchers.IO) {
                val bookInDbName = bookDao.getBySearch(pdf.name)
                if (bookInDbName == null) {
                    val doc = Document.openDocument(pdf.absolutePath, "")
                    if (doc.needsPassword()) {
                        Log.w(TAG, "Ignoring pdf file with password")
                    }
                    Log.i(TAG, "Book not already in DB, have to insert")
                    insertBook(pdf)
                }
            }
        }
    }

    // Keeping them as functions instead of variables to preserve space
    fun getAllBooks() = bookDao.getAll()
    val allBooks: LiveData<List<Book>> = bookDao.getAll().asLiveData()
    fun getStartedBooks() = bookDao.getStartedBooks()
    val startedBooks: LiveData<List<Book>> = bookDao.getStartedBooks().asLiveData()
    fun getFavoriteBooks() = bookDao.getFavoriteBooks()
    val favoriteBooks: LiveData<List<Book>> = bookDao.getFavoriteBooks().asLiveData()
//    fun getBooksBySearch(search: String) = bookDao.getBySearch(search)
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookDao.deleteBook(book)
        }
    }

    private suspend fun insertBook(file: File) {  // Should be called before the UI is even built
        val (name, path, size) = PdfUtils.getPdfInfo(file)
        val pageCount: Int? = PdfUtils.getPdfPageCount(file)
        pageCount?.let {
            val book = Book(bookName = name, bookPath = path, bookPages = pageCount, bookSize = size)
            bookDao.insertBook(book)
            Log.d(TAG, "Book: $book")
        }
    }

    fun updateIsFavorite(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = bookDao.getBookFavoriteStatus(path)
            bookDao.updateIsFavorite(path, !isFavorite)
        }
    }

    fun updateProgress(path: String, progress: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            bookDao.updateProgress(path, progress)
        }
    }

    fun getBookCover(context: Context, pdfPath: String): Bitmap? {

        val file = File(pdfPath)
        var page: PdfRenderer.Page? = null
        var pdfRenderer: PdfRenderer? = null

        try {
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor)
            page = pdfRenderer.openPage(0)
            val density = context.resources.displayMetrics.density
            val desiredWidth = (60 * density).toInt()
            val desiredHeight = (90 * density).toInt()
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true)

            page.render(scaledBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            return scaledBitmap

        } catch (e: FileNotFoundException) {
            Log.e(TAG, "Wrong path/file $e")
        } catch (e: Exception) {
            Log.e(TAG, "Error when rendering book cover: $e")
            page?.close()
            pdfRenderer?.close()
        }
        return null
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