package com.sorykhan.libroread.utils

import java.io.File

class PdfUtils {
    fun getPDFs(location: String): List<File> {
        /**
         * Input: Path String; ex. "/home/Docs"
         */
        val path = File(location)
        val allFiles: Array<File> = path.listFiles() ?: return listOf()
        return allFiles.filter { it.path.endsWith(".pdf") }
    }

    fun getPDFsRecursively(location: String = "/"): List<File> {
        /**
         * Input: A path to the starting directory to walk; default: walk the entire system
         */
        val path = File(location)
        return path.walkTopDown().filter{ it.path.endsWith(".pdf") }.toList()
    }

    fun getPdfInfo(file: File): Triple<String, String, Long> {
        return Triple(file.name, file.absolutePath, file.length())
    }

    fun getAllPdfInfo(files: List<File>): List<Triple<String, String, Long>> {
        return files.map { getPdfInfo(it) }
    }
}