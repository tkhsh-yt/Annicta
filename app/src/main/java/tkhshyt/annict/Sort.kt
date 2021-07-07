package tkhshyt.annict

enum class Sort {
    DESC, ACS;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}