package jp.ne.nissing.rakutencall

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import jp.ne.nissing.rakutencall.utils.di.DaggerRootComponent
import jp.ne.nissing.rakutencall.utils.di.applyAutoInjector
import timber.log.Timber

class MainApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerRootComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        applyAutoInjector()
    }
}
