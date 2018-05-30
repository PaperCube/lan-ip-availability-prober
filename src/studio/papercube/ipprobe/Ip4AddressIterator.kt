package studio.papercube.ipprobe

class Ip4AddressIterator(private val from: Ip4Address, private val to: Ip4Address) : Iterator<Ip4Address> {
    private var next = from

    override fun hasNext(): Boolean {
        return next <= to
    }

    override fun next(): Ip4Address {
        try {
            if (hasNext()) {
                return next
            } else throw NoSuchElementException()
        } finally {
            next += 1
        }
    }
}