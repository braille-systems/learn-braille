package com.github.braillesystems.learnbraille.data.entities

interface XmlAble {
    val xmlTag: String
    val xmlParams: Map<String, String>
    val xmlBody: HtmlText
    fun toXml(): HtmlText {
        var paramsString = ""
        for ((key, value) in xmlParams) {
            paramsString += " $key=\"$value\""
        }
        return "<$xmlTag$paramsString>\n$xmlBody\n</$xmlTag>"
    }
}