package com.sorykhan.libroread

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class PDFViewInstrumentedTest {
    @Test
    fun pdf_renderer_works() {
        onView(withId(R.id.pdf_image)).check(matches(isDisplayed()))
    }
}