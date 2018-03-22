package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Records
import javax.inject.Inject

class RecordsRepository @Inject constructor(
        val annict: AnnictService
) {

    fun records(
            version: String = "v1",
            access_token: String,
            fields: String? = null,
            filter_ids: String? = null,
            filter_episode_id: Long? = null,
            filter_has_record_comment: Boolean? = null,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String? = null,
            sort_likes_count: String? = null
    ): Single<Records> =
            annict.records(
                    version,
                    access_token,
                    fields,
                    filter_ids,
                    filter_episode_id,
                    filter_has_record_comment,
                    page,
                    per_page,
                    sort_id,
                    sort_likes_count
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }
}