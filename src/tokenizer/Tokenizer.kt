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
                input.startsWith(";", position) -> {
                    tokens.add(parseComment())
                }
                input.startsWith("box", position) -> {
                    tokens.add(parseElement("box"))
                }
                input.startsWith("text", position) -> {
                    tokens.add(parseText())
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
        val arguments = if (position < input.length && input[position] == '(') parseArguments() else emptyMap()
        skipWhitespace()
        return when (type) {
            "window" -> Token.Window(arguments)
            "box" -> Token.Box(arguments)
            else -> Token.Unknown
        }
    }

    private fun parseText(): Token {
        position += "text".length
        skipWhitespace()
        val textContent = if (position < input.length && input[position] == '(') parseTextContent() else ""
        skipWhitespace()
        return Token.Text(textContent)
    }

    private fun parseTextContent(): String {
        position++ // Skip '('
        val start = position
        while (position < input.length && input[position] != ')') {
            position++
        }
        val content = input.substring(start, position)
        if (position < input.length && input[position] == ')') {
            position++ // Skip ')'
        }
        return content
    }

    private fun parseComment(): Token {
        position += ";".length
        skipWhitespace()
        val textContent = parseCommentContent()
        skipWhitespace()
        return Token.Comment(textContent)
    }

    private fun parseCommentContent(): String {
        val start = position
        while (position < input.length && input[position] != ';') {
            position++
        }
        val content = input.substring(start, position)
        if (position < input.length && input[position] == ';') {
            position++ // Skip ';'
        }
        return content.trim() // Trim whitespace around the comment content
    }

    private fun parseArguments(): Map<String, String> {
        val arguments = mutableMapOf<String, String>()
        position++ // Skip '('
        while (position < input.length && input[position] != ')') {
            skipWhitespace()
            val key = parseIdentifier()
            skipWhitespace()
            if (position < input.length && input[position] == '=') {
                position++ // Skip '='
                skipWhitespace()
                val value = parseValue()
                arguments[key] = value
            }
            skipWhitespace()
            if (position < input.length && input[position] == ',') {
                position++ // Skip ','
            }
        }
        if (position < input.length && input[position] == ')') {
            position++ // Skip ')'
        }
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
        if (position < input.length && input[position] == '"') {
            position++ // Skip opening quote
            while (position < input.length && input[position] != '"') {
                position++
            }
            if (position < input.length && input[position] == '"') {
                position++ // Skip closing quote
            }
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
