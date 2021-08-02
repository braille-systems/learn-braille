package com.github.braillesystems.learnbraille.data.entities

import com.github.braillesystems.learnbraille.data.dsl.LessonWithSteps
import com.github.braillesystems.learnbraille.data.dsl.lessons
import org.junit.Assert.assertEquals
import org.junit.Test

import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.res.MarkerType
import com.github.braillesystems.learnbraille.res.golubinaIntroLessons
import com.github.braillesystems.learnbraille.res.SymbolType
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
    return "<p>($result)</p>"
}

internal fun toXml(material: Material): HtmlText {
    val sectionType = mapOf(
        SymbolType.ru to "RussianSymbols",
        SymbolType.digit to "Numbers",
        SymbolType.math to "ArithmeticSymbols",
        SymbolType.special to "Signs",
        SymbolType.greek to "TODO", // TODO
        SymbolType.latin to "TODO" // TODO
    )
    with(material.data) {
        val result = when (this) {
            is Symbol -> "${sectionType[this.type]}:${this.char}"
            is MarkerSymbol -> "TODO" // TODO consult @braille-systems/ios-development
        }
        return "<p>$result</p>"
    }
}

internal fun breaksToParagraphs(xmlBody: HtmlText): HtmlText {
    val regex = "<br>".toRegex()
    if (regex.find(xmlBody) == null) return xmlBody
    val result = xmlBody.replace(regex, "</p>\n<p>")
    return "<p>$result</p>"
}

internal fun toXml(stepData: StepData): HtmlText =
    when (stepData) {
        is BaseInfo -> object : XmlAble {
            override val xmlTag: String = "text"
            override val xmlParams: Map<String, String> = mapOf("type" to "info")
            override val xmlBody: HtmlText = breaksToParagraphs(stepData.text)
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
            mapOf("name" to lesson.first.name)

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
            """<reading type="reading" title="look at these dots!"><p>(F, F, F, F, F, F)</p></reading>"""
                    to ShowDots("look at these dots!", BrailleDots()),
            """<practice type="practice" title="type in these dots!"><p>(F, F, T, F, F, T)</p></practice>"""
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
