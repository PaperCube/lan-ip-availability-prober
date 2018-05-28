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
            println("Setting ip to $ip")
            try {
                val configurator = SystemNetworkConfigurator(connectionName, ip)
                val exitCode = configurator.apply()
                if (exitCode != 0) {
                    println("Unable to set ip $ip. Do you have Administrators' privilege?")
                }
            } catch (e: Exception) {
                println("Unexpected exception raised: $e")
            }

            if (NetworkConnectivity.testSingleRepeatedly("http://g.cn", 500, 10)) {
                println("SUCCESS: connected to internet successfully with config $ip")
                availableConfigs.add(ip)
            } else println("Failed.")
        }


    } catch (e: Exception) {
        System.err.println("FATAL: $e")
    }

    println("${availableConfigs.size} configs were found available")
}

fun printCommandLineHelp() {
    val commandLineHelp = """
        |usage:
        |... <connection-name> <ipv4>
        """.trimMargin()

    println(commandLineHelp)
}

