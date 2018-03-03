package tkhshyt.annict

import java.io.Serializable
import java.util.*

class Season(val year: Int, val season: Type) : Serializable {

    enum class Type {
        WINTER, SPRING, SUMMER, AUTUMN, ALL;

        fun pretty(): String {
            return when (this) {
                WINTER -> "冬"
                SPRING -> "春"
                SUMMER -> "夏"
                AUTUMN -> "秋"
                ALL -> ""
            }
        }
    }

    fun next(): Season {
        return if (season != Type.ALL) {
            val next = Type.values()[(season.ordinal + 1) % 4]
            Season(if (season == Type.AUTUMN) {
                year + 1
            } else {
                year
            }, next)
        } else {
            Season(year + 1, season)
        }
    }

    fun prev(): Season {
        return if (season != Type.ALL) {
            val prev = Type.values()[(season.ordinal + 3) % 4]
            Season(if (season == Type.WINTER) {
                year - 1
            } else {
                year
            }, prev)
        } else {
            Season(year - 1, season)
        }
    }

    fun param(): String {
        return "$year-${season.toString().toLowerCase()}"
    }

    override fun toString(): String {
        return year.toString() + season.pretty()
    }

    companion object {

        fun season(month: Int): Type {
            return when {
                month < 3 -> Type.WINTER // 1,2,3 月
                month < 6 -> Type.SPRING // 4,5,6 月
                month < 9 -> Type.SUMMER // 7,8,9 月
                else -> Type.AUTUMN      // 10,11,12 月
            }
        }

        fun season(cal: Calendar): Season {
            val season = season(cal.get(Calendar.MONTH))
            return Season(cal.get(Calendar.YEAR), season)
        }
    }
}