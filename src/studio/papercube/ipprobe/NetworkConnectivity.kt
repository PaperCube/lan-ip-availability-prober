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
        networkAddresses.map {
            //            CompletableFuture.supplyAsync({
//
//            }, networkTesterExecutor)
        }

        return 0
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