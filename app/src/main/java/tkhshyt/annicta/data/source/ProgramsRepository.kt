package tkhshyt.annicta.data.source

import io.reactivex.Single
import tkhshyt.annict.Params
import tkhshyt.annict.json.Programs
import tkhshyt.annicta.data.source.remote.ProgramsRemoteDataSource

class ProgramsRepository private constructor(val programsRemoteDataSource: ProgramsRemoteDataSource) : ProgramsDataSource {

    companion object {

        private var INSTANCE: ProgramsRepository? = null

        fun getInstance(programsRemoteDataSource: ProgramsRemoteDataSource): ProgramsRepository {
            if (INSTANCE == null) {
                INSTANCE = ProgramsRepository(programsRemoteDataSource)
            }
            return INSTANCE!!
        }
    }

    override fun getPrograms(params: Params.Programs): Single<Programs> {
        // 現状，ローカルでのデータ共有を考えていないため
        return programsRemoteDataSource.getPrograms(params)
    }
}