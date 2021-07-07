package tkhshyt.annicta.main.works

import tkhshyt.annict.Season
import tkhshyt.annict.Sort
import tkhshyt.annict.param.WorksParam

enum class SeasonSelectSpinner {
    NEXT_SEASON,
    CURRENT_SEASON,
    PREV_SEASON,
    FAVORITE,
    NEW,
    SELECT;

    fun season(): Season? {
        return when(this) {
            NEXT_SEASON -> Season.current().next()
            CURRENT_SEASON -> Season.current()
            PREV_SEASON -> Season.current().prev()
            else -> null
        }
    }

    fun genParam(): WorksParam {
        val param = WorksParam()
        when(this) {
            NEW -> {
                param.sort_id = Sort.DESC
            }
            else -> {
                param.sort_watchers_count = Sort.DESC
            }
        }
        return param
    }
}