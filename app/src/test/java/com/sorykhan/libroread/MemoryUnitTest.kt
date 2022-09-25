package com.sorykhan.libroread

import com.sorykhan.libroread.utils.getStringMemoryFormat
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MemoryUnitTest {

    @Test
    fun get_string_memory_format_test() {
        assertEquals(getStringMemoryFormat(1025L), "1.0 KB")
        assertEquals(getStringMemoryFormat(1L), "1 B")
        assertEquals(getStringMemoryFormat(0L), "0 B")
        assertEquals(getStringMemoryFormat(126_978L), "124.0 KB")
        assertEquals(getStringMemoryFormat(54_583_008L), "52.1 MB")
        assertEquals(getStringMemoryFormat(5_458_300_852L), "5.1 GB") // 5.08
    }
}