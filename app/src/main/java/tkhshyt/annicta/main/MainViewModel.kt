package tkhshyt.annicta.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import tkhshyt.annicta.data.ProgramsRepository
import tkhshyt.annicta.data.UserInfoRepository

class MainViewModel(
        context: Application,
        private val userInfoRepository: UserInfoRepository,
        private val programsRepository: ProgramsRepository
) : AndroidViewModel(context) {

}