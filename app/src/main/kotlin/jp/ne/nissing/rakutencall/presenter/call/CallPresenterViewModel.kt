package jp.ne.nissing.rakutencall.presenter.call

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class CallPresenterViewModel @Inject constructor(/* add private val UseCase here */):ViewModel(), CallContract.Presenter {
    override fun onCleared() {
        super.onCleared()
        /* TODO dispose UseCase here */
    }
}
