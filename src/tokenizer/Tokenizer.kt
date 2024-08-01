package tokenizer

class Tokenizer(private val input: String) {
    private var position = 0

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (position < input.length) {
            when {
                matchKeyword(Keywords.FRAME) -> tokens.add(parseElement(Keywords.FRAME))
                matchChar(Keywords.OPEN_BRACE) -> {
                    tokens.add(Token.Open)
                    position++
                }
                matchKeyword(Keywords.COMMENT) -> tokens.add(parseComment())
                matchKeyword(Keywords.BOX) -> tokens.add(parseElement(Keywords.BOX))
                matchKeyword(Keywords.TEXT) -> tokens.add(parseText())
                matchKeyword(Keywords.LINK) -> tokens.add(parseLink())
                matchChar(Keywords.CLOSE_BRACE) -> {
                    tokens.add(Token.End)
                    position++
                }
                matchChar(Keywords.DECLARATION) -> {
                    tokens.add(parseDeclaration())
                }
                input[position].isWhitespace() -> position++
                else -> {
                    tokens.add(parseUnknownToken())
                }
            }
        }
        return tokens
    }

    private fun matchKeyword(keyword: String): Boolean {
        if (input.startsWith(keyword, position)) {
            if (position + keyword.length >= input.length || !input[position + keyword.length].isLetterOrDigit()) {
                return true
            }
        }
        return false
    }

    private fun matchChar(char: Char): Boolean {
        return position < input.length && input[position] == char
    }

    private fun parseElement(type: String): Token {
        position += type.length
        skipWhitespace()
        val arguments = if (matchChar('(')) parseArguments() else emptyMap()
        skipWhitespace()
        return when (type) {
            Keywords.FRAME -> Token.Window(arguments)
            Keywords.BOX -> Token.Box(arguments)
            else -> Token.Unknown(type)
        }
    }

    private fun parseText(): Token {
        position += "text".length
        skipWhitespace()
        val textContent = if (matchChar('(')) parseContent() else ""
        skipWhitespace()
        return Token.Text(parseArguments(textContent))
    }

    private fun parseLink(): Token {
        position += "link".length  // Adjusted length to match the keyword
        skipWhitespace()
        val linkContent = if (matchChar('(')) parseContent() else ""
        skipWhitespace()
        return Token.Link(parseArguments(linkContent))
    }

    private fun parseContent(): String {
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

    private fun parseDeclaration(): Token {
        return TODO()
    }

    private fun parseArguments(content: String): Map<String, String> {
        val arguments = mutableMapOf<String, String>()
        val argPattern = """(\w+)\s*=\s*(".*?"|\S+)""".toRegex()
        val matches = argPattern.findAll(content)

        for (match in matches) {
            val key = match.groupValues[1]
            val value = match.groupValues[2].trim('"')
            arguments[key] = value
        }

        return arguments
    }

    private fun parseArguments(): Map<String, String> {
        val arguments = mutableMapOf<String, String>()
        position++ // Skip '('
        while (position < input.length && input[position] != ')') {
            skipWhitespace()
            val key = parseIdentifier()
            skipWhitespace()
            if (matchChar('=')) {
                position++ // Skip '='
                skipWhitespace()
                val value = parseValue()
                arguments[key] = value
            }
            skipWhitespace()
            if (matchChar(',')) {
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
        if (matchChar('"')) {
            position++ // Skip opening quote
            while (position < input.length && input[position] != '"') {
                position++
            }
            if (position < input.length && input[position] == '"') {
                position++ // Skip closing quote
            }
            return input.substring(start + 1, position - 1)
        }
        while (position < input.length && !input[position].isWhitespace() && !matchChar(',') && !matchChar(')')) {
            position++
        }
        return input.substring(start, position)
    }

    private fun parseComment(): Token {
        position += "//".length
        skipWhitespace()
        val textContent = parseCommentContent()
        return Token.Comment(textContent)
    }

    private fun parseCommentContent(): String { // Line 107
        val start = position
        while (position < input.length && input[position] != '\n') {
            position++
        }
        val content = input.substring(start, position)
        if (position < input.length && input[position] == '\n') {
            position++ // Skip '\n'
        }
        return content.trim()
    }

    private fun skipWhitespace() {
        while (position < input.length && input[position].isWhitespace()) {
            position++
        }
    }

    private fun parseUnknownToken(): Token {
        val start = position
        while (position < input.length && !input[position].isWhitespace() && input[position] != '{' && input[position] != '}' && input[position] != ';') {
            position++
        }
        val unknownToken = input.substring(start, position)
        return Token.Unknown(unknownToken)
    }
}
