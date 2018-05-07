package jp.ne.nissing.rakutencall.presenter.settings

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.ne.nissing.rakutencall.utils.annotations.ViewModelKey

@Module
abstract class SettingsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsPresenterViewModel::class)
    abstract fun bindSettingsPresenterViewModel(viewModel: SettingsPresenterViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

}
