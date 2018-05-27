package studio.papercube.ipprobe

class Ip4AddressIterator(private val from: Ip4Address, private val to: Ip4Address) : Iterator<Ip4Address> {
    private var current = from

    override fun hasNext(): Boolean {
        return current == to
    }

    override fun next(): Ip4Address {
        if (hasNext()) {
            current += 1
            return current
        } else throw NoSuchElementException()
    }
}