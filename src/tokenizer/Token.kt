package tokenizer

sealed class Token {
    data class Window(val arguments: Map<String, String>) : Token()
    data class Box(val arguments: Map<String, String>) : Token()
    object Open : Token()
    object End : Token()
    object Unknown : Token()
}
