package util

import compiler.Out
import compiler.Type
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class FileUtils {

    fun createFolder(pathString: String): Boolean {
        val path = Paths.get(pathString)
        return try {
            Files.createDirectories(path)
            Out(Type.INFO,"Directory created: $pathString","",false)
            true
        } catch (e: IOException) {
            Out(Type.ERROR,"Failed to create directory: $pathString", e.printStackTrace().toString(),true)

            false
        }
    }

    fun createFile(content: String, fileName: String, fileExtension: String, directoryPath: String?) {
        val filePath = if (directoryPath != null) {
            "$directoryPath${File.separator}$fileName.$fileExtension"
        } else {
            "$fileName.$fileExtension"
        }
        try {
            val file = File(filePath)
            file.parentFile.mkdirs() // Create parent directories if they don't exist
            file.writeText(content)
            Out(Type.INFO,"File '$filePath' created successfully.","",false)
        } catch (e: IOException) {
            Out(Type.ERROR,"Failed to create file '$filePath': ${e.message}", e.printStackTrace().toString(),true)
        }
    }

    fun getFileName(filePath: String): String {
        val file = File(filePath)
        val fileName = file.name
        return fileName.substringBeforeLast('.')
    }
}