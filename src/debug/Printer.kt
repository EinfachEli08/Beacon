package debug

import ArgumentRegistry
import compiler.web.TokenToHTML
import directory.FileReader
import tokenizer.Token
import tokenizer.Tokenizer
import util.FileUtils
import java.io.File

class Printer(private val fileReader: FileReader) {

    fun printFilePathsAndContents(rootDir: String) {
        val files = fileReader.findFiles(rootDir)
        files.forEach { file ->
            val relativePath = file.relativeTo(File(rootDir)).path

            println("Found file: $relativePath")

            if (ArgumentRegistry.isDevmode) {
                println("$relativePath contains:")
                println()
            }

            val content = fileReader.readFileContents(file)

            if (ArgumentRegistry.isDevmode) {
                println(content)
                println()
                println("tokenized content:")
            }

            val tokenizer = Tokenizer(content)
            val tokens = tokenizer.tokenize()

            println("Tokenizing file: $relativePath")

            tokens.forEach { token ->
                when (token) {
                    is Token.Window -> println("Window with arguments: ${token.arguments}")
                    is Token.Box -> println("Box with arguments: ${token.arguments}")
                    is Token.Text -> println("Text with arguments: ${token.arguments}")
                    is Token.Comment -> println("Comment: ${token.arguments}")
                    Token.Open -> println("Opening block")
                    Token.End -> println("End of block")
                    Token.Unknown -> println("Unknown token")
                }
            }

            val converter = TokenToHTML()
            val xml = converter.convert(tokens)

            println("Generated XML:")
            println(xml)

            val fUtil = FileUtils()
            val filePath = File("$rootDir\\web\\$relativePath").parent

            fUtil.createFolder(filePath)
            fUtil.createFile(xml,fUtil.getFileName(relativePath),"html",filePath)
            println()
        }
    }
}
