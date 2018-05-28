package studio.papercube.ipprobe

fun main(args: Array<String>) {
    var availableConfigs: MutableList<Ip4Address> = ArrayList()
    try {
        if (args.size < 2) {
            printCommandLineHelp()
            return
        }

        val connectionName = args[0]
        val ipString = args[1]

        for (ip in Ip4AddressRange.of(ipString)) {
            println()

            print("Testing $ip ")
            try {
                val configurator = SystemNetworkConfigurator(connectionName, ip)
                val exitCode = configurator.apply()
                if (exitCode != 0) {
                    print("Unable to set ip $ip. Do you have Administrators' privilege?")
                }
            } catch (e: Exception) {
                print("Unexpected exception raised: $e")
            }

            if (NetworkConnectivity.testSingleRepeatedly("http://g.cn", 1000, 20) { "." }) {
                print("  SUCCESS: $ip")
                availableConfigs.add(ip)
            } else print("Connection failed.")
        }


    } catch (e: Exception) {
        System.err.println("FATAL: $e")
    }

    println()
    println("${availableConfigs.size} configs were found available")
    availableConfigs.forEach(::println)

}

fun printCommandLineHelp() {
    val commandLineHelp = """
        |usage:
        |... <connection-name> <ipv4>
        """.trimMargin()

    println(commandLineHelp)
}

