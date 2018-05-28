package studio.papercube.ipprobe

class Ip4AddressIterator(private val from: Ip4Address, private val to: Ip4Address) : Iterator<Ip4Address> {
    private var current = from
    private var firstElementConsumed = false

    override fun hasNext(): Boolean {
        return current < to
    }

    override fun next(): Ip4Address {
        try {
            if (hasNext()) {
                if (firstElementConsumed) current += 1
                return current
            } else throw NoSuchElementException()
        } finally {
            firstElementConsumed = true
        }
    }
}