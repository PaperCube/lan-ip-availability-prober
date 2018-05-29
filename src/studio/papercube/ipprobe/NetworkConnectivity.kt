package studio.papercube.ipprobe

import java.io.IOException
import java.net.MalformedURLException
import java.net.URI
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

object NetworkConnectivity {
    @Volatile
    private var networkTesterCounter = 0
    private val networkTesterExecutor = Executors.newCachedThreadPool(ThreadFactory {
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
    fun testSingleRepeatedly(networkAddress: String, interval: Long, repeatCount: Int, onRetry: ((Int)->Unit)? = null): Boolean {
        for (i in 1..repeatCount) {
            try {
                if (testSingleAddress(networkAddress)) {
                    return true
                }
            } catch (e: Exception) {
                onRetry?.invoke(i)
            }

            try {
                Thread.sleep(interval)
            } catch (e: InterruptedException) {
                //ignore
            }
        }

        return false
    }

    private fun testSingleAddress(networkAddress: String): Boolean {
        return try {
            val uri = URI(networkAddress)
            val conn = uri.toURL().openConnection()
            conn.connectTimeout = 10000
            conn.getInputStream().read()
            true
        } catch (e: MalformedURLException) {
            throw e
        } catch (e: IOException) {
            false
        }
    }
}