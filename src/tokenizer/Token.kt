package tokenizer

sealed class Token {
    object Open : Token()
    object End : Token()
    data class Window(val arguments: Map<String, String>) : Token()
    data class Box(val arguments: Map<String, String>) : Token()
    data class Text(val arguments: Map<String, String>) : Token()
    data class Link(val arguments: Map<String, String>) : Token()
    data class Comment(val content: String) : Token()
    data class Unknown(val value: String) : Token()
}
