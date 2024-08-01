package tokenizer

object Keywords {
    const val FRAME = "Frame"
    const val BOX = "Box"
    const val TEXT = "Text"
    const val LINK = "Link"
    const val COMMENT = "//"
    const val OPEN_BRACE = '{'
    const val CLOSE_BRACE = '}'
    const val DECLARATION = '.'
}

fun matchStringToKeyword(value: String): String? {

    Keywords::class.java.declaredFields.forEach { field ->
        if (field.type == String::class.java && field.get(null)?.toString()?.equals(value, ignoreCase = true) == true) {
            return field.get(null).toString()
        }
    }

    return null
}
