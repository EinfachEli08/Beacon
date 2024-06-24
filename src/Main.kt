import debug.Printer
import directory.FileReader

fun main(args: Array<String>) {

    ArgumentRegistry.initialize(args)

    val mode = ArgumentRegistry.mode
    val dir = ArgumentRegistry.dir
    val isDevmode = ArgumentRegistry.isDevmode

    println("Dev mode: $isDevmode")

    if (dir.isNullOrBlank() || mode.isNullOrBlank()) {
        println("Usage: beacon -comp <platforms (f.e. web, mobile)> -dir <project root directory> (optional: -d -> (used to track bugs while tokenizing))")
    } else {
        val components = mode.split(",").map { it.trim() }

        if (components.isNotEmpty()) {
            components.forEach { comp ->
                when (comp) {
                    "web" -> {
                        println("Started compilation for web")
                        println("Opening directory: $dir")
                        // compile to HTML, and later even <CSS and JS>

                        val fileReader = FileReader()
                        val printer = Printer(fileReader)
                        printer.printFilePathsAndContents(dir)
                    }
                    "mobile" -> {
                        println("Started compilation for mobile")
                        println("Opening directory: $dir")
                    }
                    "all" -> {
                        println("Started compilation for all platforms")
                        println("Opening directory: $dir")
                    }
                    else -> {
                        println("Unknown platform: $comp")
                        println("currently supported platforms are: web, mobile, all")
                    }
                }
            }
        } else {
            println("No components specified for compilation.")
        }
    }
}

