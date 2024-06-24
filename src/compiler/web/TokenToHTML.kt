package compiler.web

import tokenizer.Token

class TokenToHTML {
    fun convert(tokens: List<Token>): String {
        val xmlBuilder = StringBuilder()
        val openTags = mutableListOf<String>()
        var indentLevel = 0

        fun appendIndentedLine(line: String) {
            for (i in 0..<indentLevel) {
                xmlBuilder.append("    ")
            }
            xmlBuilder.append(line).append("\n")
        }

        appendIndentedLine("<!DOCTYPE html>")
        appendIndentedLine("<html lang=\"en\">")
        indentLevel++
        appendIndentedLine("<head>")
        indentLevel++


        for (token in tokens) {
            when (token) {
                is Token.Window -> {
                    if(token.arguments["charset"] != null) {
                        appendIndentedLine("<meta charset=${token.arguments["charset"]}>")
                    }
                    appendIndentedLine("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                    appendIndentedLine("<title>${token.arguments["title"]}</title>")
                    indentLevel--
                    appendIndentedLine("</head>")
                    appendIndentedLine("<body>")
                    indentLevel++
                }
                is Token.Box -> {
                    val id = token.arguments["id"]
                    if (id != null) {
                        appendIndentedLine("<div id=\"$id\">")
                    } else {
                        appendIndentedLine("<div>")
                    }
                    openTags.add("div")
                    indentLevel++
                }
                is Token.Text -> {
                    appendIndentedLine("<span>${token.arguments}</span>")
                }
                is Token.Comment -> {
                    appendIndentedLine("<!-- ${token.arguments} -->")
                }
                Token.Open -> {
                    // No specific action required for Open tokens, already handled by Box or Window tokens.
                }
                Token.End -> {
                    indentLevel--
                    if (openTags.isNotEmpty()) {
                        val tag = openTags.removeAt(openTags.lastIndex)
                        appendIndentedLine("</$tag>")
                    }
                }
                Token.Unknown -> {
                    // Handle unknown tokens if necessary
                }
            }
        }

        // Close any remaining open tags
        while (openTags.isNotEmpty()) {
            indentLevel--
            val tag = openTags.removeAt(openTags.lastIndex)
            appendIndentedLine("</$tag>")
        }

        indentLevel--
        appendIndentedLine("</body>")
        appendIndentedLine("</html>")

        return xmlBuilder.toString()
    }
}