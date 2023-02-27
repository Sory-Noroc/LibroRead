package com.sorykhan.libroread.utils

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private const val TAG = "PdfUtils"

class PdfUtils {

    companion object {

        const val downloadsPath = "/storage/emulated/0/Download/"

        @RequiresApi(Build.VERSION_CODES.R)
        fun hasAllFilesPermission() = Environment.isExternalStorageManager()

        fun getDownloadsFiles(context: Context): Array<String> {
            /**
             *
             */
            if (Build.VERSION.SDK_INT >= 30) {
                if (hasAllFilesPermission()) {
                    return File(downloadsPath).list() ?: emptyArray()
                } else {
                    requestFilePermission(context)

                    val filesArray = File(downloadsPath).list()
                    if (filesArray != null) {
                        return filesArray
                    } else {
                        getDownloadsFiles(context)
                    }
                }
            } else {
                return File(downloadsPath).list() ?: emptyArray()
            }
            return emptyArray()
        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun requestFilePermission(context: Context) {
            context.startActivity(
                Intent(
                    Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION,
                )
            )
        }

        fun getPDFs(location: String): List<File> {
            /**
             * Input: Path String; ex. "/home/Docs"
             */
            val path = File(location)
            val allFiles: Array<File> = path.listFiles() ?: return listOf()
            //        Log.i(TAG, "Pdf Files extracted: $pdfFiles")
            return allFiles.filter { it.path.endsWith(".pdf") }
        }

        fun getPDFsRecursively(location: String = "/"): List<File> {
            /**
             * Input: A path to the starting directory to walk; default: walk the entire system
             */
            val path = File(location)
            return path.walkTopDown().filter { it.path.endsWith(".pdf") }.toList()
        }

        fun getPdfInfo(file: File): Triple<String, String, Long> {
            return Triple(file.name, file.absolutePath, file.length())
        }

        fun getAllPdfInfo(files: List<File>): List<Triple<String, String, Long>> {
            return files.map { getPdfInfo(it) }
        }

        fun getPdfPageCount(pdfFile: File): Int? {
            val parcelFileDescriptor =
                ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            var pdfRenderer: PdfRenderer? = null
            try {
                pdfRenderer = PdfRenderer(parcelFileDescriptor)
            } catch (e: SecurityException) {
                Log.e(TAG, "$e")
            }
            val pageCount = pdfRenderer?.pageCount
            pdfRenderer?.close()
            return pageCount
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
