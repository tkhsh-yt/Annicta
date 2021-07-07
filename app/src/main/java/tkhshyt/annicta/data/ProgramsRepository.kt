package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Programs
import javax.inject.Inject

class ProgramsRepository @Inject constructor(
        private val annict: AnnictService
) {

    fun programs(
            version: String = "v1",
            access_token: String,
            fields: String? = null,
            filter_ids: String? = null,
            filter_channel_ids: String? = null,
            filter_work_ids: String? = null,
            filter_started_at_gt: String? = null,
            filter_started_at_lt: String? = null,
            filter_unwatched: Boolean = true,
            filter_rebroadcast: Boolean? = null,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String? = null,
            sort_started_at: String = "desc"
    ): Single<Programs> =
            annict.programs(
                    version,
                    access_token,
                    fields,
                    filter_ids,
                    filter_channel_ids,
                    filter_work_ids,
                    filter_started_at_gt,
                    filter_started_at_lt,
                    filter_unwatched,
                    filter_rebroadcast,
                    page,
                    per_page,
                    sort_id,
                    sort_started_at
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }
}