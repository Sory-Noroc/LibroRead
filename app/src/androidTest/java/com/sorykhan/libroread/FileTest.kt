package com.sorykhan.libroread

import android.os.Environment
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FileTest {

//    @Test
//    fun get_pdf_page_count() {
//        val pdfFile: File = PdfUtils.getPDFs("C:\\Users\\Admin\\OneDrive\\Desktop\\CS Books\\")[0]
//        assertEquals(PdfUtils.getPdfPageCount(pdfFile), 120)
//    }

    @Test
    fun get_downloads_location() {
        assertEquals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),
        "/storage/emulated/0/Download")
    }
}
