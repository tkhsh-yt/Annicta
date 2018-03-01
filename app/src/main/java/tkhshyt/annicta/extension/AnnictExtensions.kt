package tkhshyt.annicta.extension

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Activities
import tkhshyt.annict.json.Work
import tkhshyt.annict.json.Works

fun <T> Single<T>.defaultOn() = this.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun AnnictService.embedStatus(
        works: List<Work>,
        access_token : String,
        sort_watchers_count : String? = null
) : ((List<Work>) -> Unit, () -> Unit, (Throwable) -> Unit) -> Unit {
    return { success, final, fail ->
        this.followingWorks(
                access_token = access_token,
                sort_watchers_count = sort_watchers_count,
                filter_ids = works.map { it.id }.joinToString(",")
        ).defaultOn()
            .doFinally(final)
            .subscribe({
                val statusWorks = it.body().resources()
                val merge = works.map { work ->
                    statusWorks.find { it.id == work.id } ?: work
                }

                success(merge)
            }, { fail(it) })
    }
}

fun AnnictService.worksWithStatus(
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
): ((Works) -> Unit, () -> Unit, (Throwable) -> Unit) -> Unit {
    return { success, final, fail ->
        this.works(
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
        ).defaultOn()
            .subscribe({
                val works = it.body()

                this.embedStatus(
                        works.works,
                        access_token,
                        sort_watchers_count
                )({
                    success(works.copy(works = it))
                }, final, fail)
            }, { fail(it) })
    }
}

fun AnnictService.followingActivitiesWithStatus(
        version: String = "v1",
        access_token: String,
        fields: String = "",
        filter_actions: String = "",
        filter_muted: Boolean = true,
        page: Int = 1,
        per_page: Int = 25,
        sort_id: String = "desc"
): ((Activities) -> Unit, () -> Unit, (Throwable) -> Unit) -> Unit {
    return { success, final, fail ->
        this.followingActivities(
                version,
                access_token,
                fields,
                filter_actions,
                filter_muted,
                page,
                per_page,
                sort_id
        ).defaultOn()
            .subscribe({
                val activities = it.body()
                val works = activities.resources().mapNotNull { it.work }

                this.embedStatus(
                        works,
                        access_token
                )({ newWorks ->
                    val newActivities = (1 until activities.activities.size).map {
                        activities.activities[it].copy(work = newWorks[it])
                    }
                    success(activities.copy(activities = newActivities))
                }, final, fail)
            }, { fail(it) })
    }
}

