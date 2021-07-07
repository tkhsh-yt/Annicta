package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Episodes
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
        private val annict: AnnictService
) {

    fun episodes(
            version: String = "v1",
            access_token: String,
            fields: String? = null,
            filter_ids: String? = null,
            filter_work_id: String? = null,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String? = null,
            sort_sort_number: String = "desc"
    ): Single<Episodes> =
            annict.episodes(
                    version,
                    access_token,
                    fields,
                    filter_ids,
                    filter_work_id,
                    page,
                    per_page,
                    sort_id,
                    sort_sort_number
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }
}