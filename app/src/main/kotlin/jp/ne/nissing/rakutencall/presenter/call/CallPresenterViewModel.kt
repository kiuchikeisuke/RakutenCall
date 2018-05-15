package jp.ne.nissing.rakutencall.presenter.call

import android.arch.lifecycle.ViewModel
import jp.ne.nissing.rakutencall.data.entity.number.TelephoneNumber
import jp.ne.nissing.rakutencall.domain.call.GetUsePackageInfo
import jp.ne.nissing.rakutencall.domain.call.ValidatePhoneNumber
import javax.inject.Inject

class CallPresenterViewModel @Inject constructor(
        private val validatePhoneNumber: ValidatePhoneNumber, private val getUsePackageInfo: GetUsePackageInfo) : ViewModel(), CallContract.Presenter {

    override fun loadPhoneApp(next: (GetUsePackageInfo.Response) -> Unit) {
        getUsePackageInfo.execute(next = next)
    }

    override fun validateTelephoneNumber(originalTelephoneNumber: TelephoneNumber, next: (ValidatePhoneNumber.Response) -> Unit, error: (Throwable) -> Unit) {
        validatePhoneNumber.execute(ValidatePhoneNumber.Request(originalTelephoneNumber.generatePhoneNumber()), next, error)
    }

    override fun onCleared() {
        super.onCleared()
        /* dispose UseCase here */
        validatePhoneNumber.dispose()
        getUsePackageInfo.dispose()
    }
}
