package com.github.braillesystems.learnbraille.data.entities

import org.junit.Assert.assertEquals
import org.junit.Test

import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E

class ToXmlTest {
    @Test
    fun stepsToXml() {
        val cases = mapOf<String, StepData>(
            """<text type="info">dummy text</text>""" to Info("dummy text"),
            """<text type="info">dummy last text</text>""" to LastInfo("dummy last text"),
            """<reading type="reading" title="look at these dots!">(F, F, F, F, F, F)</reading>"""
                    to ShowDots("look at these dots!", BrailleDots()),
            """<practice type="practice" title="type in these dots!">(F, F, T, F, F, T)</practice>"""
                    to InputDots("type in these dots!", BrailleDots(E, E, F, E, E, F))
        )
        for ((expectedXml, stepData) in cases) {
            assertEquals(expectedXml, stepData.toXml().replace("\n", ""))
        }
    }
}
