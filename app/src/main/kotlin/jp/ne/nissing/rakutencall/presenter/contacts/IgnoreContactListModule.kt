package jp.ne.nissing.rakutencall.presenter.contacts

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.ne.nissing.rakutencall.utils.annotations.ViewModelKey

@Module
abstract class IgnoreContactListModule {
    @Binds
    @IntoMap
    @ViewModelKey(IgnoreContactListPresenterViewModel::class)
    abstract fun bindIgnoreContactListPresenterViewModel(viewModel: IgnoreContactListPresenterViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeIgnoreContactListFragment(): IgnoreContactListFragment
}
