package tokenizer

sealed class Token {
    data class Window(val arguments: Map<String, String>) : Token()
    data class Box(val arguments: Map<String, String>) : Token()
    data class Text(val arguments: String) : Token()
    data class Comment(val arguments: String) : Token()
    data object Open : Token()
    data object End : Token()
    data object Unknown : Token()
}
