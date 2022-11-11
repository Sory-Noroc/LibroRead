package com.sorykhan.libroread

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTests {
    @Test
    fun navigate_to_favorite_fragment() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
    }
}