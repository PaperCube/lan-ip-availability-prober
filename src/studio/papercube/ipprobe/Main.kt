package studio.papercube.ipprobe

import studio.papercube.library.argparser.Parameter

fun main(args: Array<String>) {
    val availableConfigs: MutableList<Ip4Address> = ArrayList()
    try {
        val parameter = Parameter.resolve(args)
        val argUnnamed = parameter.getUnnamedArg()
        if (argUnnamed == null || args.size < 2) {
            printCommandLineHelp()
            return
        }

        val connectionName = argUnnamed[0]
        val ipString = argUnnamed[1]
        val ipEndString = argUnnamed.getOrNull(2)
        val timeOutArg = parameter.getSingleValue("--timeout") ?: "20"

        val testHostName = parameter.getSingleValue("--test-host-name") ?: "g.cn"
        val timeOutSeconds: Long = timeOutArg.toLongOrNull() ?: kotlin.run {
            println("Invalid value for --timeout. Setting to default value 20")
            20L
        }

        val ipRange = Ip4Address.parseMin(ipString)..Ip4Address.parseMax(ipEndString ?: ipString)

        for (ip in ipRange) {
            println()

            print("Testing $ip ")

            val textStatus = RewritableText()

            try {
                val configurator = SystemNetworkConfigurator(connectionName, ip)
                val exitCode = configurator.apply()
                if (exitCode != 0) {
                    textStatus.write("Unable to set ip $ip. Do you have Administrators' privilege?")
                    continue
                }
            } catch (e: Exception) {
                print("Unexpected exception raised: $e")
            }

            val connectivityTester = NetworkConnectivityTester(testHostName, timeOutSeconds * 1000)
            var elapsedSeconds = 0

            connectivityTester.start()

            while (!connectivityTester.isDone()) {
                textStatus.write("Checking network connectivity (${elapsedSeconds++}s/${timeOutSeconds}s)")
                connectivityTester.waitFor(1000)
            }

            if (connectivityTester.get()) {
                textStatus.write("Passed.")
                availableConfigs.add(ip)
            } else textStatus.write("Connection failed.")
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
        |... <connection-name> <ipv4> [ipv4-end] [options]
        |
        |Options:
        |   --test-host-name <host-name>    the host name to test network connectivity against
        |   --timeout <seconds>             how long this test should be regarded invalid if network check fails
        """.trimMargin()

    println(commandLineHelp)
}

