package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Works
import tkhshyt.annict.param.WorksParam
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

    fun works(param: WorksParam): Single<Works> =
            annict.works(
                    param.version,
                    param.access_token,
                    param.fields?.joinToString(separator = ","),
                    param.filter_ids?.joinToString(separator = ","),
                    param.filter_season?.param(),
                    param.filter_title,
                    param.page,
                    param.per_page,
                    param.sort_id?.toString(),
                    param.sort_season?.toString(),
                    param.sort_watchers_count?.toString()
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

    fun worksWithStatus(param: WorksParam): Single<Works> =
            works(param).flatMap {
                    followingWorks(
                            version = param.version,
                            access_token = param.access_token,
                            filter_ids = it.works.map { it.id }.joinToString(separator = ","),
                            sort_id = param.sort_id?.toString(),
                            sort_watchers_count = param.sort_watchers_count?.toString(),
                            sort_season = param.sort_season?.toString(),
                            per_page = param.per_page
                    ).zipWith(Single.just(it), BiFunction<Works, Works, Works> { filter, source ->
                        source.copy(
                                works = source.works.map { work ->
                                    filter.works.find { it.id == work.id } ?: work
                                }
                        )
                    })
                }
}
