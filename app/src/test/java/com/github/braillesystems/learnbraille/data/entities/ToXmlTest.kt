package com.github.braillesystems.learnbraille.data.entities

import com.github.braillesystems.learnbraille.data.dsl.LessonWithSteps
import com.github.braillesystems.learnbraille.data.dsl.lessons
import org.junit.Assert.assertEquals
import org.junit.Test

import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.res.golubinaIntroLessons
import java.io.File

internal val testLessons by lessons {
    lesson(name = "first lesson") {
        +FirstInfo("first title")
    }
}

internal fun toXml(lesson: LessonWithSteps): HtmlText {
    return object : XmlAble {
        override val xmlTag: String = "lesson"

        override val xmlParams: Map<String, String>
            get() = mapOf("name" to lesson.first.name.replace("\"", "'"))

        override val xmlBody: HtmlText
            get() = {
              var stepBuilder: HtmlText = ""
                for(step in lesson.second.dropLast(1)){
                    stepBuilder += (step.first.data.toXml() + "\n") // TODO [... <br>] -> [<p>...</p>]
                }
                stepBuilder + lesson.second.takeLast(1)[0].first.data.toXml()
            }()
    }.toXml()
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
        println(toXml(testLessons.lessons[0]))

        var xmlText = ""
        for(lessons in golubinaIntroLessons.lessons) {
            xmlText += (toXml(lessons) + "\n")
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
