object ArgumentRegistry {
    private lateinit var argsMap: Map<String, String?>

    fun initialize(args: Array<String>) {
        argsMap = parseArguments(args)
    }

    val mode: String? get() = argsMap["-comp"]
    val dir: String? get() = argsMap["-dir"]
    val isDevmode: Boolean get() = argsMap.containsKey("-d")

    private fun parseArguments(args: Array<String>): Map<String, String?> {
        val argsMap = mutableMapOf<String, String?>()
        var i = 0
        while (i < args.size) {
            if (args[i].startsWith("-")) {
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    argsMap[args[i]] = args[i + 1]
                    i += 2
                } else {
                    argsMap[args[i]] = null
                    i++
                }
            } else {
                i++
            }
        }
        return argsMap
    }
}
