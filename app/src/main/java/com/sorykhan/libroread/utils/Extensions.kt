package com.sorykhan.libroread.utils

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.sorykhan.libroread.database.Book
import com.sorykhan.libroread.external.DocumentActivity
import java.io.File

fun Fragment.startBookActivity(book: Book) {
    val file = File(book.bookPath)
    val documentUri: Uri = Uri.fromFile(file)
    val intent = Intent(context, DocumentActivity::class.java)
    intent.action = Intent.ACTION_VIEW
    intent.data = documentUri
    intent.putExtra("path", book.bookPath)
    startActivity(intent)
}