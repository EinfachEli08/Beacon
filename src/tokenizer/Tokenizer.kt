package tokenizer

class Tokenizer(private val input: String) {
    private var position = 0

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (position < input.length) {
            when {
                input.startsWith("window", position) -> {
                    tokens.add(parseElement("window"))
                }
                input.startsWith("{", position) -> {
                    tokens.add(Token.Open)
                    position++
                }
                input.startsWith("box", position) -> {
                    tokens.add(parseElement("box"))
                }
                input.startsWith("}", position) -> {
                    tokens.add(Token.End)
                    position++
                }
                input[position].isWhitespace() -> position++
                else -> {
                    tokens.add(Token.Unknown)
                    position++
                }
            }
        }
        return tokens
    }

    private fun parseElement(type: String): Token {
        position += type.length
        skipWhitespace()
        val arguments = if (input[position] == '(') parseArguments() else emptyMap()
        skipWhitespace()
        if (type == "window") {
            return Token.Window(arguments)
        }
        return Token.Box(arguments)
    }

    private fun parseArguments(): Map<String, String> {
        val arguments = mutableMapOf<String, String>()
        position++ // Skip '('
        while (position < input.length && input[position] != ')') {
            skipWhitespace()
            val key = parseIdentifier()
            skipWhitespace()
            if (input[position] == '=') {
                position++ // Skip '='
                skipWhitespace()
                val value = parseValue()
                arguments[key] = value
            }
            skipWhitespace()
            if (input[position] == ',') {
                position++ // Skip ','
            }
        }
        position++ // Skip ')'
        return arguments
    }

    private fun parseIdentifier(): String {
        val start = position
        while (position < input.length && input[position].isLetterOrDigit()) {
            position++
        }
        return input.substring(start, position)
    }

    private fun parseValue(): String {
        val start = position
        if (input[position] == '"') {
            position++ // Skip opening quote
            while (position < input.length && input[position] != '"') {
                position++
            }
            position++ // Skip closing quote
            return input.substring(start + 1, position - 1)
        }
        while (position < input.length && !input[position].isWhitespace() && input[position] != ',' && input[position] != ')') {
            position++
        }
        return input.substring(start, position)
    }

    private fun skipWhitespace() {
        while (position < input.length && input[position].isWhitespace()) {
            position++
        }
    }
}