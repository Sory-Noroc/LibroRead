package com.sorykhan.libroread

import com.sorykhan.libroread.utils.PdfUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File

class FileUnitTest {
    private val utils = PdfUtils()

    @Test
    fun file_name_test() {
        assertEquals(File("").name, "")
        assertEquals(
            File("C:\\Users\\Admin\\OneDrive\\Documente\\AdeverintaMedicala.pdf").name,
            "AdeverintaMedicala.pdf"
        )
    }

    @Test
    fun file_size_test() {
        /* Size, not Size on Disk */
        val file = File("C:\\Users\\Admin\\OneDrive\\Documente\\AdeverintaMedicala.pdf")
        assertEquals(file.length(), 1_390_023L)
    }

    @Test
    fun file_path_test() {
        val path = "C:\\Users\\Admin\\Downloads\\CerereBucuresti.pdf"
        val file = File(path)
        assertEquals(file.path, path)
    }

    @Test
    fun get_pdf_info_test() {
        val path = "C:\\Users\\Admin\\Downloads\\ConfirmareCraiova.pdf"
        val file = File(path)
        assertEquals(utils.getPdfInfo(file), Triple("ConfirmareCraiova.pdf", path, 339_184L))
    }

    @Test
    fun get_PDFs_from_Documents_test() {
        val pdfDocsList = utils.getPDFs("C:\\Users\\Admin\\OneDrive\\Documente")
        assertEquals(pdfDocsList, listOf(
            File("C:\\Users\\Admin\\OneDrive\\Documente\\AdeverintaMedicala.pdf")
        ))
    }

    @Test
    fun get_PDFs_from_nothing_test() {
        val emptyList = utils.getPDFs("")
        assertEquals(emptyList, listOf<File>())
    }

    @Test
    fun get_all_PDFs_from_desktop_test() {
        val pdfList = utils.getPDFsRecursively("C:\\Users\\Admin\\OneDrive\\Desktop").map { it.name }
        assertEquals(
            pdfList,
            listOf("XII_Biologia (a. 2017, in limba romana).pdf", "XII_Chimia (a. 2017, in limba romana).pdf", "XII_Fizica - Astronomie (a. 2017, in limba romana).pdf", "XII_Geografia (in limba romana).pdf", "XII_Informatica (in limba romana) (1).pdf", "XII_Istoria romanilor si universala (a. 2013, in limba romana).pdf", "XII_Limba engleza.pdf", "XII_Limba si literatura romana.pdf", "XII_Matematica (a. 2017, in limba romana).pdf", "XI_Fizica (a. 2020 in limba romana).pdf", "XI_Informatica (in limba romana).pdf", "X_Fizica (in limba romana).pdf", "X_Matematica (in limba romana).pdf", "automate the boring stuff with python automate the boring stuff with python ( PDFDrive ).pdf", "Computer Systems A Programmers Perspective.pdf", "Computer Systems Digital Design, Fundamentals of Computer Architecture and Assembly Language ( PDFDrive ).pdf", "Data Structures and Algorithms Using Python.pdf", "Grokking Algorithms.pdf", "Learn Kotlin For Android Development.pdf", "pdfcoffee.com_android-development-with-kotlin-pdf-free.pdf", "thinkpython.pdf", "liana_rh413.pdf", "LIANA_RH414D.pdf", "OrgDesk.pdf", "scan0101mate.pdf", "X_Informatica (in limba romana).pdf")
        )
    }

    @Test
    fun get_pdf_info_from_documents_test() {
        val pdfList = utils.getAllPdfInfo(utils.getPDFs("C:\\Users\\Admin\\OneDrive\\Documente"))
        assertEquals(
            pdfList,
            listOf(Triple(
                "AdeverintaMedicala.pdf",
                "C:\\Users\\Admin\\OneDrive\\Documente\\AdeverintaMedicala.pdf",
                1_390_023L
            ))
        )
    }
}