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

internal fun toXml(brailleDots: BrailleDots): HtmlText {
    val dotsSymbols = brailleDots.toString().replace("F", "T").replace("E", "F")
    var result = ""
    dotsSymbols.dropLast(1).forEach { result += "$it, " }
    result += dotsSymbols.takeLast(1)
    return "($result)"
}

internal fun toXml(material: Material): HtmlText {
    return "TODO" // TODO (not easy)
}

internal fun toXml(stepData: StepData): HtmlText =
    when (stepData) {
        is BaseInfo -> object : XmlAble {
            override val xmlTag: String = "text"
            override val xmlParams: Map<String, String> = mapOf("type" to "info")
            override val xmlBody: HtmlText = stepData.text
        }
        is BaseInput -> object : XmlAble {
            override val xmlTag: String = "practice"
            override val xmlParams = mapOf(
                "type" to "practice",
                "title" to if (stepData is InputDots) stepData.text ?: "" else ""
            )
            override val xmlBody: HtmlText = when (stepData) {
                is InputDots -> toXml(stepData.brailleDots)
                is Input -> toXml(stepData.material)
                else -> "toXml not implemented for this class"
            }
        }
        is BaseShow -> object : XmlAble {
            override val xmlTag: String = "reading"
            override val xmlParams = mapOf(
                "type" to "reading",
                "title" to if (stepData is ShowDots) stepData.text ?: "" else ""
            )
            override val xmlBody: HtmlText = when (stepData) {
                is ShowDots -> toXml(stepData.brailleDots)
                is Input -> toXml(stepData.material)
                else -> "toXml not implemented for this class"
            }
        }
        else -> object : XmlAble {
            override val xmlTag: String = stepData.toString()
            override val xmlParams: Map<String, String> = mapOf()
            override val xmlBody: HtmlText = "toXml not implemented for this class"
        }
    }.toXml()

internal fun toXml(lesson: LessonWithSteps): HtmlText {
    return object : XmlAble {
        override val xmlTag: String = "lesson"

        override val xmlParams: Map<String, String> =
            mapOf("name" to lesson.first.name.replace("\"", "'"))

        override val xmlBody: HtmlText
            get() = {
                var stepBuilder: HtmlText = ""
                for (step in lesson.second.dropLast(1)) {
                    stepBuilder += (toXml(step.first.data) + "\n") // TODO [... <br>] -> [<p>...</p>]
                }
                stepBuilder + toXml(lesson.second.takeLast(1)[0].first.data)
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
            assertEquals(expectedXml, toXml(stepData).replace("\n", ""))
        }
    }

    @Test
    fun lessonToXml() {
        println(toXml(testLessons.lessons[0]))

        var xmlText = ""
        for (lessons in golubinaIntroLessons.lessons) {
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
