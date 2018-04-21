package tkhshyt.annict.param

import io.reactivex.Single
import retrofit2.Response
import tkhshyt.annict.AnnictService
import tkhshyt.annict.Sort
import tkhshyt.annict.json.Programs
import java.text.SimpleDateFormat
import java.util.*

class ProgramsParam {
    var version: String = "v1"
    var access_token: String = ""
    var fields: Collection<String>? = null
    var filter_ids: Collection<Long>? = null
    var filter_channel_ids: Collection<Long>? = null
    var filter_work_ids: Collection<Long>? = null
    var filter_started_at_gt: Date? = null
    var filter_started_at_lt: Date? = null
    var filter_unwatched: Boolean? = true
    var filter_rebroadcast: Boolean? = null
    var page: Int = 1
    var per_page: Int = 25
    var sort_id: Sort? = null
    var sort_started_at: Sort? = Sort.DESC

    companion object {
        private val startedAtFormat = SimpleDateFormat("yyyy/MM/dd HH/mm", Locale.JAPAN)
    }

    fun build(annict: AnnictService): Single<Response<Programs>> {
        return annict.programs(
                version,
                access_token,
                fields?.joinToString(separator = ","),
                filter_ids?.joinToString(separator = ","),
                filter_channel_ids?.joinToString(separator = ","),
                filter_work_ids?.joinToString(separator = ","),
                filter_started_at_gt?.let { startedAtFormat.format(it) },
                filter_started_at_lt?.let { startedAtFormat.format(it) },
                filter_unwatched,
                filter_rebroadcast,
                page,
                per_page,
                sort_id?.toString(),
                sort_started_at?.toString()
        )
    }
}
