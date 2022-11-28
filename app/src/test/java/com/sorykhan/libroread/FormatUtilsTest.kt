package com.sorykhan.libroread

import com.sorykhan.libroread.utils.FormatUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test


class FormatUtilsTest {

    @Test
    fun getProgressPercentageTest() {
        assertEquals(FormatUtils.getProgressPercentage(100, 1000), "10%")
        assertEquals(FormatUtils.getProgressPercentage(-100, 1000), "0%")
        assertEquals(FormatUtils.getProgressPercentage(10000, 1000), "100%")
        assertEquals(FormatUtils.getProgressPercentage(1000, 1000), "100%")
        assertEquals(FormatUtils.getProgressPercentage(101, 657), "15%")
    }
}