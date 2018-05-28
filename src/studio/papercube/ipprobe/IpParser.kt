package studio.papercube.ipprobe

object IpParser {
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

    private fun List<String>.toIntArray(): IntArray {
        return IntArray(size) { i ->
            this[i].toInt()
        }
    }
}