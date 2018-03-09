package tkhshyt.annicta

import dagger.Component
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.auth.AuthFragment
import tkhshyt.annicta.work.EpisodeListFragment
import tkhshyt.annicta.main.*
import tkhshyt.annicta.record.RecordActivity
import tkhshyt.annicta.record.RecordListFragment
import tkhshyt.annicta.top.TopActivity
import tkhshyt.annicta.work.WorkActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [(ReleaseModule::class)])
interface AppComponent {

    fun inject(fragment: AuthActivity)
    fun inject(fragment: AuthFragment)

    fun inject(activity: TopActivity)

    fun inject(activity: MainActivity)
    fun inject(fragment: WorkListFragment)
    fun inject(fragment: ProgramListFragment)

    fun inject(fragment: RecordListFragment)
    fun inject(activity: RecordActivity)

    fun inject(activity: WorkActivity)
    fun inject(fragment: EpisodeListFragment)

    fun inject(fragment: ActivityListFragment)

    fun inject(item: WorkItem.ViewHolder)
}
