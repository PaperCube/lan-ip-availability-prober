package studio.papercube.ipprobe

class SystemNetworkConfigurator(var connectionName: String,
                                var ip: Ip4Address,
                                var netmask: Ip4Address = Ip4Address(255, 255, 255, 0),
                                var gateway: Ip4Address? = null) {
    private var process: Process? = null
    fun apply(): Int {
        val commandArgs = "netsh interface ipv4 set address $connectionName static $ip $netmask $gateway 1"
        val proc = Runtime.getRuntime().exec(commandArgs)
        process = proc
        proc.waitFor()
        return proc.exitValue()
    }
}