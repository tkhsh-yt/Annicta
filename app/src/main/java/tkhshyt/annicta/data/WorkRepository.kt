package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import tkhshyt.annict.AnnictService
import javax.inject.Inject

class WorkRepository @Inject constructor(
        val annict: AnnictService
) {

    fun updateState(
            version: String = "v1",
            access_token: String,
            work_id: Long,
            kind: String
    ): Single<Response<Void>> =
            annict.updateState(
                    version, access_token, work_id, kind
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
}