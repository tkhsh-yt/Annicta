package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Works
import javax.inject.Inject

class WorksRepository @Inject constructor(
        val annict: AnnictService
) {

    fun works(
            version: String = "v1",
            access_token: String,
            fields: String? = null,
            filter_ids: String? = null,
            filter_season: String? = null,
            filter_title: String? = null,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String? = null,
            sort_season: String? = null,
            sort_watchers_count: String? = null
    ): Single<Works> =
            annict.works(
                    version,
                    access_token,
                    fields,
                    filter_ids,
                    filter_season,
                    filter_title,
                    page,
                    per_page,
                    sort_id,
                    sort_season,
                    sort_watchers_count
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }

    fun followingWorks(
            version: String = "v1",
            access_token: String,
            fields: String? = null,
            filter_ids: String? = null,
            filter_season: String? = null,
            filter_title: String? = null,
            filter_status: String? = null,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String? = null,
            sort_season: String? = null,
            sort_watchers_count: String? = null
    ): Single<Works> =
            annict.followingWorks(
                    version,
                    access_token,
                    fields,
                    filter_ids,
                    filter_season,
                    filter_title,
                    filter_status,
                    page,
                    per_page,
                    sort_id,
                    sort_season,
                    sort_watchers_count
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }

    fun worksWithStatus(
            version: String = "v1",
            access_token: String,
            fields: String? = null,
            filter_ids: String? = null,
            filter_season: String? = null,
            filter_title: String? = null,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String? = null,
            sort_season: String? = null,
            sort_watchers_count: String? = null
    ): Single<Works> =
            works(
                    version,
                    access_token,
                    fields, filter_ids,
                    filter_season,
                    filter_title,
                    page,
                    per_page,
                    sort_id,
                    sort_season,
                    sort_watchers_count
            ).flatMap {
                    followingWorks(
                            version = version,
                            access_token = access_token,
                            filter_ids = it.works.map { it.id }.joinToString(separator = ","),
                            sort_id = sort_id,
                            sort_watchers_count = sort_watchers_count,
                            sort_season = sort_season,
                            per_page = per_page
                    ).zipWith(Single.just(it), BiFunction<Works, Works, Works> { filter, source ->
                        source.copy(
                                works = source.works.map { work ->
                                    filter.works.find { it.id == work.id } ?: work
                                }
                        )
                    })
                }
}
