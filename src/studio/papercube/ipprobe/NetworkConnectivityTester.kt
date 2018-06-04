package studio.papercube.ipprobe

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class NetworkConnectivityTester(private val hostName: String,
                                private val timeoutMillis: Long) {
    private val testThread = TestThread()
    private val valueModificationLock = ReentrantLock()
    private var value = false
    private var isDone = false

    private inner class TestThread : Thread() {
        override fun run() {
            var result = false
            try {
                val start = System.currentTimeMillis()
                while (System.currentTimeMillis() - start < timeoutMillis && !isInterrupted) {
                    if (NetworkConnectivity.testSingleAddress(hostName)) {
                        result = true
                        break
                    }
                    Thread.sleep(10L)
                }
            } catch (e: InterruptedException) {
                //ignored
            } finally {
                completeWithValue(result)
            }
        }
    }

    fun waitFor() = testThread.join()

    fun waitFor(millis: Long) = testThread.join(millis)

    fun cancelAwait() {
        testThread.interrupt()
        testThread.join()
    }

    fun isDone(): Boolean = valueModificationLock.withLock {
        isDone
    }

    fun get(): Boolean = valueModificationLock.withLock {
        value
    }

    fun start() = testThread.start()

    private fun completeWithValue(flag: Boolean) {
        valueModificationLock.withLock {
            value = flag
            isDone = true
        }
    }
}