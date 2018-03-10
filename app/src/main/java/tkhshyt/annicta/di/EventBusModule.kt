package tkhshyt.annicta.di

import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
class EventBusModule(private val eventBus: EventBus) {

    @Provides
    @Singleton
    fun provideEventBus(): EventBus {
        return eventBus
    }
}