package studio.papercube.ipprobe

fun main(args: Array<String>) {
    val availableConfigs: MutableList<Ip4Address> = ArrayList()
    try {
        if (args.size < 2) {
            printCommandLineHelp()
            return
        }

        val connectionName = args[0]
        val ipString = args[1]
        val ipEndString = args.getOrNull(2)

        val ipRange = Ip4Address.parseMin(ipString)..Ip4Address.parseMax(ipEndString ?: ipString)

        for (ip in ipRange) {
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

            if (
                    NetworkConnectivity.testSingleRepeatedly(
                            "g.cn",
                            1000,
                            20) { print('.') }
            ) {
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
        |... <connection-name> <ipv4> [ipv4-end]
        """.trimMargin()

    println(commandLineHelp)
}

