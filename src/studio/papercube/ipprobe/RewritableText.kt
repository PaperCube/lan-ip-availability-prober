package studio.papercube.ipprobe

import java.io.PrintStream

open class RewritableText(private val outputStream: PrintStream = System.out) {
    private var lastLength = 0

    open fun write(string: String) {
        for (i in 0 until lastLength) {
            outputStream.print("\b")
        }

        outputStream.print(string)

        if (string.length < lastLength) {
            val cleanCnt = lastLength - string.length
            for (i in 0 until cleanCnt) {
                outputStream.print(" ")
            }

            for (i in 0 until cleanCnt) {
                outputStream.print('\b')
            }
        }

        lastLength = string.length
    }

    open fun clear() = write("")
}