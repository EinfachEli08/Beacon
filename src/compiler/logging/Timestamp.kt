package compiler.logging

import java.text.SimpleDateFormat
import java.util.Date


fun addTimestamp(layout: Array<String>): String {
    val date = Date()
    val dateFormatMap = mapOf(
        "hh" to "HH",
        "mm" to "mm",
        "ss" to "ss",
        "ll" to "SSS"
    )

    // Create format string based on the layout array
    val formatString = layout.joinToString(":") { part ->
        dateFormatMap[part] ?: throw IllegalArgumentException("Unknown format part: $part")
    }
    val formatter = SimpleDateFormat(formatString)
    val output = " [" + formatter.format(date) + "]"
    return output
}
