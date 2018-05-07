package jp.ne.nissing.rakutencall.utils.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.ne.nissing.rakutencall.presenter.call.CallActivity
import jp.ne.nissing.rakutencall.presenter.call.CallModule
import jp.ne.nissing.rakutencall.presenter.settings.SettingsActivity
import jp.ne.nissing.rakutencall.presenter.settings.SettingsModule
import jp.ne.nissing.rakutencall.utils.annotations.ActivityScope

/* bind modules for Presenter's modules */
@Module
internal abstract class BindingModules {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /*If you add a new presenter(activity, broadcast, service), add a new contributeMethod. For detail, refer to FIXME of each presenter's module */
    @ContributesAndroidInjector(modules = [CallModule::class])
    @ActivityScope
    abstract fun contributeCallActivityInjector(): CallActivity

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    @ActivityScope
    abstract fun contributeSettingsActivityInjector(): SettingsActivity
}
