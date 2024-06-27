package debug

import ArgumentRegistry
import compiler.Out
import compiler.Type
import compiler.web.TokenToHTML
import directory.FileReader
import tokenizer.Token
import tokenizer.Tokenizer
import tokenizer.matchStringToKeyword
import util.FileUtils
import java.io.File

class Printer(private val fileReader: FileReader) {

    fun printFilePathsAndContents(rootDir: String) {
        val files = fileReader.findFiles(rootDir)
        files.forEach { file ->
            val relativePath = file.relativeTo(File(rootDir)).path
            val content = fileReader.readFileContents(file)

            Out(Type.INFO,"Found file: $relativePath","",false)

            if (ArgumentRegistry.isDevmode) {
                println()
                Out(Type.INFO,"$relativePath contains:",content,false)
                println()
            }

            val tokenizer = Tokenizer(content)
            val tokens = tokenizer.tokenize()

            Out(Type.INFO,"Tokenizing file: $relativePath","",false)

            tokens.forEach { token ->
                when (token) {
                    is Token.Window -> Out(Type.INFO,"Window with arguments: ${token.arguments}","",false)
                    is Token.Box -> Out(Type.INFO,"Box with arguments: ${token.arguments}","",false)
                    is Token.Text -> Out(Type.INFO,"Text with arguments: ${token.arguments}","",false)
                    is Token.Link -> Out(Type.INFO,"Link with arguments: ${token.arguments}","",false)
                    is Token.Comment -> Out(Type.INFO,"Comment: ${token.content}","",false)
                    is Token.Open -> Out(Type.INFO,"Opening block","",false)
                    is Token.End -> Out(Type.INFO,"End of block","",false)
                    is Token.Unknown -> {

                        Out(Type.ERROR, "Unknown token -> \"${token.value}\" <- found in file: ${relativePath}","${
                            if(matchStringToKeyword(token.value)?.isNotEmpty() == true){
                                Out(Type.NONE,"Did you mean \"${matchStringToKeyword(token.value)}\" instead of \"${token.value}\"?","",false)
                            }else{} 
                                    
                        }" ,false)

                        Out(Type.NONE,"Please correct the issue and recompile your program!","",false)

                        println("")
                        Out(Type.WARNING,"Beacon will exit now","",true)
                    }
                }
            }

            val converter = TokenToHTML()
            val xml = converter.convert(tokens)

            println()
            Out(Type.INFO,"Generated XML:",xml,false)


            val fUtil = FileUtils()
            val filePath = File("$rootDir\\web\\$relativePath").parent

            fUtil.createFolder(filePath)
            fUtil.createFile(xml,fUtil.getFileName(relativePath),"html",filePath)
            println()
        }
    }
}
