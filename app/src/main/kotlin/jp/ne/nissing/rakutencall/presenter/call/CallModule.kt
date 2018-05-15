package jp.ne.nissing.rakutencall.presenter.call

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.ne.nissing.rakutencall.utils.annotations.ViewModelKey

@Module
abstract class CallModule {
    @Binds
    @IntoMap
    @ViewModelKey(CallPresenterViewModel::class)
    abstract fun bindCallPresenterViewModel(viewModel: CallPresenterViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeCallFragment(): CallFragment
}
