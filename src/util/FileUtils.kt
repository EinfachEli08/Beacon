package util

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class FileUtils {

    fun createFolder(pathString: String): Boolean {
        val path = Paths.get(pathString)
        return try {
            Files.createDirectories(path)
            println("Directory created: $pathString")
            true
        } catch (e: IOException) {
            println("Failed to create directory: $pathString")
            e.printStackTrace()
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
            println("File '$filePath' created successfully.")
        } catch (e: IOException) {
            println("Error creating file '$filePath': ${e.message}")
        }
    }

    fun getFileName(filePath: String): String {
        val file = File(filePath)
        val fileName = file.name
        return fileName.substringBeforeLast('.')
    }
}