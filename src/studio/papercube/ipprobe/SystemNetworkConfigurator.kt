package studio.papercube.ipprobe

class SystemNetworkConfigurator(var connectionName: String,
                                var ip: Ip4Address,
                                var netmask: Ip4Address = Ip4Address(255, 255, 255, 0),
                                var gateway: Ip4Address? = null) {
    private var process: Process? = null

    fun apply(): Int {
        val configuratorProcess = Runtime.getRuntime().exec(command)
        process = configuratorProcess
        configuratorProcess.waitFor()
        return configuratorProcess.exitValue()
    }

    val command:String
        get() = "netsh interface ipv4 set address $connectionName static $ip $netmask ${gateway()} 1"

    private fun gateway(): Ip4Address {
        return gateway ?: inferGateway(ip)
    }

    private fun inferGateway(ip: Ip4Address) = Ip4Address(ip[0], ip[1], ip[2], 1)
}