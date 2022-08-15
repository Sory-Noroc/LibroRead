package com.sorykhan.libroread

import com.sorykhan.libroread.utils.MemoryFormat
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MemoryUnitTest {

    @Test
    fun get_string_memory_format_test() {
        assertEquals(MemoryFormat().getStringMemoryFormat(1025L), "1.0 KB")
        assertEquals(MemoryFormat().getStringMemoryFormat(1L), "1 B")
        assertEquals(MemoryFormat().getStringMemoryFormat(0L), "0 B")
        assertEquals(MemoryFormat().getStringMemoryFormat(126_978L), "124.0 KB")
        assertEquals(MemoryFormat().getStringMemoryFormat(54_583_008L), "52.1 MB")
        assertEquals(MemoryFormat().getStringMemoryFormat(5_458_300_852L), "5.1 GB") // 5.08
    }
}