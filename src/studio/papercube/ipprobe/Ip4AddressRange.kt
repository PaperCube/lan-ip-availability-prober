package studio.papercube.ipprobe

class Ip4AddressRange(private val from: Ip4Address, private val to: Ip4Address):Iterable<Ip4Address>{
    companion object {
        fun of(ipString: String) = IpParser.parseMin(ipString)..IpParser.parseMax(ipString)
    }

    override fun iterator(): Iterator<Ip4Address> {
        return Ip4AddressIterator(from, to)
    }
}