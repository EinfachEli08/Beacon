package compiler.web

import compiler.Out
import compiler.Type
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
                    if(token.arguments["encoding"] != null) {
                        appendIndentedLine("<meta charset=${token.arguments["encoding"]}>")
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
                    if(token.arguments["type"]?.isNotEmpty() == true){
                        if(token.arguments["type"] == "heading"){
                            appendIndentedLine("<h1>${token.arguments["name"]}</h1>")
                        }else if(token.arguments["type"] == "paragraph") {
                            appendIndentedLine("<p>${token.arguments["name"]}</p>")
                        }else if(token.arguments["type"] == "span") {
                            appendIndentedLine("<span>${token.arguments["name"]}</span>")
                        }else{
                            Out(Type.WARNING,"Unrecognized type: ${token.arguments["type"]} ", "This either needs to be implemented or doesn't exist and will be ignored",false)
                        }
                    }else{
                        Out(Type.WARNING,"No type set!", "You didn't set a type in code, please set one. Manually setting to span",false)
                        appendIndentedLine("<span>${token.arguments["name"]}</span>")
                    }

                }
                is Token.Link -> {
                    val href = token.arguments["href"] ?: ""
                    val target = token.arguments["target"] ?: ""
                    val attributes = if (target.isNotEmpty()) " href=\"$href\" target=\"$target\"" else " href=\"$href\""
                    appendIndentedLine("<a$attributes>${token.arguments["name"] ?: ""}</a>")
                }
                is Token.Comment -> {
                    appendIndentedLine("<!-- ${token.content} -->")
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

                is Token.Unknown -> println("Unknown token encountered while creating file: $token")
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