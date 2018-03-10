package tkhshyt.annicta.data.source.remote

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.Params
import tkhshyt.annict.json.Programs
import tkhshyt.annicta.data.source.ProgramsDataSource

class ProgramsRemoteDataSource private constructor(val accessToken: String) : ProgramsDataSource {

    companion object {
        private var INSTANCE: ProgramsRemoteDataSource? = null

        fun getInstance(accessToken: String): ProgramsRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = ProgramsRemoteDataSource(accessToken)
            }
            return INSTANCE!!
        }
    }

    override fun getPrograms(params: Params.Programs): Single<Programs> {
        val annict = AnnictFactory.create()
        return annict.programs(
                version = params.version,
                access_token = accessToken,
                fields = params.fields,
                filter_ids = params.filter_ids,
                filter_channel_ids =  params.filter_channel_ids,
                filter_work_ids = params.filter_work_ids,
                filter_started_at_gt = params.filter_started_at_gt,
                filter_started_at_lt = params.filter_started_at_lt,
                filter_unwatched = params.filter_unwatched,
                filter_rebroadcast = params.filter_rebroadcast,
                page = params.page,
                per_page = params.per_page,
                sort_id = params.sort_id,
                sort_started_at = params.sort_started_at
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.body() }
    }
}