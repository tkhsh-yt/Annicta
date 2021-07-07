package tkhshyt.annicta.data

import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Activities
import tkhshyt.annict.json.Works
import javax.inject.Inject

class ActivitiesRepository @Inject constructor(
        private val annict: AnnictService,
        private val worksRepository: WorksRepository
) {

    fun followingActivities(
            version: String = "v1",
            access_token: String,
            fields: String = "",
            filter_actions: String = "",
            filter_muted: Boolean = true,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String = "desc"
    ): Single<Activities> =
            annict.followingActivities(
                    version,
                    access_token,
                    fields,
                    filter_actions,
                    filter_muted,
                    page,
                    per_page,
                    sort_id
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }

    fun followingActivitiesWithStatus(
            version: String = "v1",
            access_token: String,
            fields: String = "",
            filter_actions: String = "",
            filter_muted: Boolean = true,
            page: Int = 1,
            per_page: Int = 25,
            sort_id: String = "desc"
    ): Single<Activities> =
            followingActivities(
                    version,
                    access_token,
                    fields,
                    filter_actions,
                    filter_muted,
                    page,
                    per_page,
                    sort_id
            ).flatMap {
                worksRepository.followingWorks(
                        version = version,
                        access_token = access_token,
                        filter_ids = it.activities.map { it.work?.id }.distinct().joinToString(separator = ","),
                        sort_id = sort_id,
                        per_page = per_page
                ).zipWith(Single.just(it), BiFunction<Works, Activities, Activities> { filter, source ->
                    source.copy(
                            activities = source.activities.map { activity ->
                                activity.copy(
                                        work = filter.works.find { it.id == activity.work?.id } ?: activity.work
                                )
                            }
                    )
                })
            }
}