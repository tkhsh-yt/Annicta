package tkhshyt.annict.param

import io.reactivex.Single
import retrofit2.Response
import tkhshyt.annict.AnnictService
import tkhshyt.annict.Season
import tkhshyt.annict.Sort
import tkhshyt.annict.json.Works

class WorksParam {
    var version: String = "v1"
    var access_token: String = ""
    var fields: Collection<Long>? = null
    var filter_ids: Collection<Long>? = null
    var filter_season: Season? = null
    var filter_title: String? = null
    var page: Int = 1
    var per_page: Int = 25
    var sort_id: Sort? = null
    var sort_season: Sort? = null
    var sort_watchers_count: Sort? = null

    fun build(annict: AnnictService): Single<Response<Works>> {
        return annict.works(
                version,
                access_token,
                fields?.joinToString(separator = ","),
                filter_ids?.joinToString(separator = ","),
                filter_season?.param(),
                filter_title,
                page,
                per_page,
                sort_id?.toString(),
                sort_season?.toString(),
                sort_watchers_count?.toString()
        )
    }
}