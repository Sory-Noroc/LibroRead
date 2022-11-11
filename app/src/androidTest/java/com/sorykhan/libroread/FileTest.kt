package com.sorykhan.libroread

import android.os.Environment
import android.provider.MediaStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sorykhan.libroread.utils.PdfUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class FileTest {
    private val utils = PdfUtils()

    @Test
    fun testDownloadsFolderAccess() {
        TODO()
    }

    @Test
    fun get_pdf_page_count() {
        val pdfFile: File = utils.getPDFs("C:\\Users\\Admin\\OneDrive\\Desktop\\CS Books\\")[0]
        assertEquals(utils.getPdfPageCount(pdfFile), 120)
    }
}
