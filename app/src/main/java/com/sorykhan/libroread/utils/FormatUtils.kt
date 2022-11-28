package com.sorykhan.libroread.utils

import kotlin.math.roundToInt

class FormatUtils {
    companion object {
        fun getProgressPercentage(progress: Int, totalPages: Int): String {
            try {
                val ratio: Double = progress.toDouble() / totalPages
                if (ratio < 0) {
                    return "0%"
                } else if (ratio > 1) {
                    return "100%"
                }
                val roundOff = (ratio * 100.0).roundToInt()
                return "$roundOff%"

            } catch (e: Exception) {
                return "0%"
            }
        }
    }
}