package compiler.logging

import kotlin.system.exitProcess

object Type {
    const val INFO = "INFO"
    const val WARNING = "WARNING"
    const val ERROR = "ERROR"
    const val NONE = ""
}

fun Out(type: String, title: String, message: String, shouldTerminate: Boolean) {
    when (type) {
        Type.INFO -> {
            println("[INFO] $title ${if (message.isNotEmpty()) "\n$message" else ""}")
        }
        Type.WARNING -> {
            println("[WARNING] $title ${if (message.isNotEmpty()) "\n$message" else ""}")
        }
        Type.ERROR -> {
            println("[ERROR] $title ${if (message.isNotEmpty()) "\n$message" else ""}")
        }
        Type.NONE -> {
            println("$title ${if (message.isNotEmpty()) "\n$message" else ""}")
        }
        else -> {
            println("[UNKNOWN TYPE] $title ${if (message.isNotEmpty()) "\n$message" else ""}")
        }
    }

    if (shouldTerminate) {
        exitProcess(0)
    }
}

fun main() {
    Out(Type.INFO, "Title for INFO", "", false)
    Out(Type.WARNING, "Title for WARNING", "This is a warning message", false)
    Out(Type.ERROR, "Title for ERROR", "This is an error message", true)
}
