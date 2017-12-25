package tkhshyt.annict

enum class Season {
    WINTER, SPRING, SUMMER, AUTUMN;

    fun next() = {
        when (this) {
            WINTER -> SPRING
            SPRING -> SUMMER
            SUMMER -> AUTUMN
            AUTUMN -> WINTER
        }
    }

    override fun toString(): String {
        return super.toString().toLowerCase()
    }

    companion object {

        fun season(month: Int) = {
            if(month < 4)
                WINTER
            else if(month < 7)
                SPRING
            else if(month < 10)
                SUMMER
            else
                AUTUMN
        }
    }
}