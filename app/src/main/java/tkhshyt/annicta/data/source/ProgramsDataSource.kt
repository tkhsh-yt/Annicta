package tkhshyt.annicta.data.source

import io.reactivex.Single
import tkhshyt.annict.Params
import tkhshyt.annict.json.Programs

interface ProgramsDataSource {

    fun getPrograms(params: Params.Programs): Single<Programs>
}