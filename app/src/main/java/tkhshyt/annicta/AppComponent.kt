package tkhshyt.annicta

import dagger.Component
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
}
