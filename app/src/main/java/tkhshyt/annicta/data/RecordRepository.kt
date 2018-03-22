package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Record
import javax.inject.Inject

class RecordRepository @Inject constructor(
        val annict: AnnictService
) {

    fun createRecord(
            version: String = "v1",
            access_token: String,
            episode_id: Long,
            comment: String? = null,
            rating_state: String? = null,
            share_twitter: Boolean = false,
            share_facebook: Boolean = false
    ): Single<Record> =
            annict.createRecord(
                    version,
                    access_token,
                    episode_id,
                    comment,
                    rating_state,
                    share_twitter,
                    share_facebook
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body() }
}