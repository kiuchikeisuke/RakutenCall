package jp.ne.nissing.rakutencall.presenter.call

import android.arch.lifecycle.ViewModel
import com.example.example.utils.annotations.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CallModule {
    @Binds @IntoMap @ViewModelKey(CallPresenterViewModel::class) abstract fun bindCallPresenterViewModel(viewModel: CallPresenterViewModel): ViewModel
    @ContributesAndroidInjector abstract fun contributeCallFragment():CallFragment

    /* FIXME MUST add below method to BindingModules */
    // @ContributesAndroidInjector(modules = [CallModule::class]) @ActivityScope abstract fun contributeCallActivityInjector(): CallActivity
}
