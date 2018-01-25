package tkhshyt.annicta

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ReleaseModule::class)])
interface AppComponent {

    fun inject(fragment: ProgramFragment)

    fun inject(fragment: WorkFragment)

    fun inject(fragment: AuthFragment)

    fun inject(activity: MainActivity)
}
