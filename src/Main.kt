import compiler.logging.Out
import compiler.logging.Type
import out.Printer
import directory.FileReader

fun main(args: Array<String>) {

    ArgumentRegistry.initialize(args)

    val mode = ArgumentRegistry.mode
    val dir = ArgumentRegistry.dir
    val isDevmode = ArgumentRegistry.isDevmode

    Out(Type.WARNING,"Dev mode: $isDevmode","",false)

    if (dir.isNullOrBlank() || mode.isNullOrBlank()) {
        Out(Type.ERROR,"Usage: beacon -comp <platforms (f.e. web, mobile)> -dir <project root directory> (optional: -d -> (used to track bugs while tokenizing))", "",true)
    } else {
        val components = mode.split(",").map { it.trim() }

        if (components.isNotEmpty()) {
            components.forEach { comp ->
                when (comp) {
                    "web" -> {
                        Out(Type.INFO,"Started compilation for web","Opening directory: $dir",false)
                        println()
                        // compile to HTML, and later even <CSS and JS>

                        val fileReader = FileReader()
                        val printer = Printer(fileReader)
                        printer.printFilePathsAndContents(dir)
                    }
                    "mobile" -> {
                        Out(Type.INFO,"Started compilation for mobile","Opening directory: $dir",false)
                    }
                    "all" -> {
                        Out(Type.INFO,"Started compilation for all platforms","Opening directory: $dir",false)
                    }
                    else -> {
                        Out(Type.ERROR,"Unknown platform: $comp","currently supported platforms are: web, mobile, all",true)

                    }
                }
            }
        } else {
            Out(Type.ERROR,"No components specified for compilation.","",true)
        }
    }
}

