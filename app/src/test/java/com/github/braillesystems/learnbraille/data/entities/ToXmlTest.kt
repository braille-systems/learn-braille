package com.github.braillesystems.learnbraille.data.entities

import com.github.braillesystems.learnbraille.data.dsl.lessons
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.res.golubinaIntroLessons
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal val testLessons by lessons {
    lesson(name = "first lesson") {
        +FirstInfo("first title")
    }
}

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

    @Test
    fun lessonToXml() {
        println(testLessons.lessons[0].toXml())

        var xmlText = ""
        for (lessons in golubinaIntroLessons.lessons) {
            xmlText += (lessons.toXml() + "\n")
        }
        val xmlLinesList = xmlText.split("\n")
        File("course.xml").printWriter().use { out ->
            out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            out.println("<root>")
            xmlLinesList.forEach { out.println(it) }
            out.println("</root>")
        }
    }
}
