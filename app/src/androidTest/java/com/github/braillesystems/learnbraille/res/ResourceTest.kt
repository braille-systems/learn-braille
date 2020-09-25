package com.github.braillesystems.learnbraille.res

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.braillesystems.learnbraille.utils.get
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResourceTest {

    private lateinit var context: Context

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun inputSymbolPrintRulesTest() {
        content.symbols.keys.forEach {
            assertNotNull(context.inputSymbolPrintRules[it])
        }
    }

    @Test
    fun showSymbolPrintRulesTest() {
        content.symbols.keys.forEach {
            assertNotNull(context.showSymbolPrintRules[it])
        }
    }

    @Test
    fun inputMarkerPrintRulesTest() {
        content.markers.keys.forEach {
            assertNotNull(context.inputMarkerPrintRules[it])
        }
    }

    @Test
    fun showMarkerPrintRulesTest() {
        content.markers.keys.forEach {
            assertNotNull(context.showMarkerPrintRules[it])
        }
    }
}