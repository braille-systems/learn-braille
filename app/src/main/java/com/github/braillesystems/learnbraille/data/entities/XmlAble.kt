package com.github.braillesystems.learnbraille.data.entities

interface XmlAble {
    val xmlTag: String
    val xmlParams: Map<String, String>
    val xmlBody: HtmlText
    fun toXml(): HtmlText {
        var paramsString = ""
        for ((key, value) in xmlParams) {
            val formattedValue = value.replace("\"", "'")
            paramsString += " $key=\"$formattedValue\""
        }

        val formattedBody = xmlBody.replace("\n +".toRegex(), "\n")
        return "<$xmlTag$paramsString>\n$formattedBody\n</$xmlTag>"
    }
}
