package studio.papercube.ipprobe

import java.io.IOException
import java.net.InetAddress
import java.net.MalformedURLException
import java.util.concurrent.Executors

object NetworkConnectivity {
    @Volatile
    private var networkTesterCounter = 0
    private val networkTesterExecutor = Executors.newCachedThreadPool({
        Thread(it).apply {
            name = "Network Tester Thread - ${++networkTesterCounter}"
        }
    })


    /**
     * Test given whether network addresses are accessible
     *
     * @return amount of addresses that are accessible
     * @throws IllegalArgumentException if argument count is zero
     */
    @Suppress("UNUSED_PARAMETER")
    fun test(vararg networkAddresses: String): Int {
//        networkAddresses.map {
//                        CompletableFuture.supplyAsync({
//
//            }, networkTesterExecutor)
//        }
//
//        return 0
        throw NotImplementedError("Unimplemented. Use testSingleAddress(String) instead")
    }

    /**
     * Test one address repeatedly. This is especially useful when an ip change takes some time to come into effect.
     * Normally, this method never throws
     *
     * @param networkAddress the network address to test
     * @param interval interval between two retries, in millis
     * @param repeatCount after how many failures should this method return
     *
     * @return a boolean, true if connection is available, false otherwise
     *
     * @see [testSingleAddress]
     */
    fun testSingleRepeatedly(networkAddress: String, interval: Long, repeatCount: Int, onRetry: ((Int) -> Unit)? = null): Boolean {
        for (i in 1..repeatCount) {
            if (testSingleAddress(networkAddress)) {
                return true
            }

            try {
                Thread.sleep(interval)
            } catch (e: InterruptedException) {
                //ignore
            }
            onRetry?.invoke(i)
        }

        return false
    }

    fun testSingleAddress(networkAddress: String): Boolean {
        return try {
            val inetAddress = InetAddress.getByName(networkAddress)
            inetAddress.isReachable(5000)
        } catch (e: MalformedURLException) {
            throw e
        } catch (e: IOException) {
            false
        }
    }
}