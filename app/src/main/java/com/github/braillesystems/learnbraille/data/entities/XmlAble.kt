package com.github.braillesystems.learnbraille.data.entities

interface XmlAble {
    val xmlTag: String
    val xmlParams: Map<String, String>
    val xmlBody: HtmlText
    fun toXml(): HtmlText {
        val paramsStringBuilder = StringBuilder()
        for ((key, value) in xmlParams) {
            val formattedValue = value.replace("\"", "'").replace("<br>", "")
            paramsStringBuilder.append(" $key=\"$formattedValue\"")
        }

        val formattedBody = xmlBody.replace("\n +".toRegex(), "\n")
        return "<$xmlTag$paramsStringBuilder>\n$formattedBody\n</$xmlTag>"
    }
}
