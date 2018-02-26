package tkhshyt.annicta.extension

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Works

fun <T> Single<T>.defaultOn() = this.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun AnnictService.embedStatus(
        works: Works,
        access_token : String,
        sort_watchers_count : String? = "desc"
) : ((Works) -> Unit, () -> Unit, (Throwable) -> Unit) -> Unit {
    return { success, final, fail ->
        this.followingWorks(
                access_token = access_token,
                sort_watchers_count = sort_watchers_count,
                filter_ids = works.resources().map { it.id }.joinToString(",")
        ).defaultOn()
            .doFinally(final)
            .subscribe({
                val statusWorks = it.body().resources()
                val merge = works.resources().map { work ->
                    statusWorks.find { it.id == work.id } ?: work
                }

                success(Works(merge, works.total_count, works.next_page, works.prev_page))
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
                        works,
                        access_token,
                        sort_watchers_count
                )(success, final, fail)
            }, { fail(it) })
    }
}