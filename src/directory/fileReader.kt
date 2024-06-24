package directory

import java.io.File

class FileReader {

    fun findFiles(rootDir: String, ): List<File> {
        val root = File(rootDir)
        if (!root.isDirectory) {
            throw IllegalArgumentException("Provided path is not a directory")
        }
        return root.walkTopDown()
            .filter { it.isFile && it.extension == "bc" }
            .toList()
    }

    fun readFileContents(file: File): String {
        return file.readText()
    }
}