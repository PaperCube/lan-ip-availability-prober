package studio.papercube.ipprobe

import java.util.*

class Ip4Address(address: IntArray) {
    companion object {
        /**
         * Parses either full ip or partial ip.
         * Partial ip refers to those whose several parts in the end are omitted.
         * This method will return the smallest possible ip.
         *
         * For example, it will return 172.28.71.0 with an input value 172.28.71
         *
         * @param ip an ip address represented in string
         *
         * @return an [Ip4Address] corresponding to input
         */
        fun parseMin(ip: String): Ip4Address {
            val parts = try {
                ip.split('.').toIntArray()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("$ip is not a valid ip")
            }
            val size = parts.size
            return Ip4Address(parts)
        }

        private fun List<String>.toIntArray(): IntArray {
            return IntArray(size) { i ->
                this[i].toInt()
            }
        }

        /**
         * Parses either full ip or partial ip.
         * Partial ip refers to those whose several parts in the end are omitted.
         * This method will return the largest possible ip.
         *
         * For example, it will return 172.28.71.0 with an input value 172.28.71
         *
         * @param ip an ip address represented in string
         *
         * @return an [Ip4Address] corresponding to input
         */
        fun parseMax(ip: String): Ip4Address {
            val parts = ip.split('.').toIntArray()
            val size = parts.size
            val addressArray = IntArray(4) { i ->
                if (i >= size) 255 else parts[i]
            }
            return Ip4Address(addressArray)
        }


    }

    private val address = IntArray(4)

    init {
        for (i in 0..3) {
            this.address[i] = address.getOrNull(i) ?: break
        }
    }

    constructor(p1: Int, p2: Int, p3: Int, p4: Int) : this(intArrayOf(p1, p2, p3, p4))

    operator fun plus(inc: Int): Ip4Address {
        val newAddress = Ip4Address(address)
        var increment = inc
        for (i in 3 downTo 0) {
            val value = address[i] + increment;
            newAddress.address[i] = value % 256
            increment = value / 256
            if (increment == 0) {
                break
            }

        }

        if (increment > 0) {
            return Ip4Address(0, 0, 0, 0) + (increment - 1)
        }

        return newAddress
    }

    operator fun compareTo(another: Ip4Address): Int {
        return Integer.compare(toInt(), another.toInt())
    }

    private fun toInt(): Int {
        return (address[0] and 0xFF shl 24) or
                (address[1] and 0xFF shl 16) or
                (address[2] and 0xFF shl 8) or
                (address[3] and 0xFF)
    }

    override fun toString(): String {
        return address.joinToString(separator = ".")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is Ip4Address) {
            Arrays.equals(address, other.address)
        } else false
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(address)
    }

    operator fun rangeTo(end: Ip4Address): Ip4AddressRange {
        return Ip4AddressRange(this, end)
    }

    operator fun get(index: Int): Int {
        return this.address[index]
    }
}