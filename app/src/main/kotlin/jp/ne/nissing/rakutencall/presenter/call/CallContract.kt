package jp.ne.nissing.rakutencall.presenter.call

import jp.ne.nissing.rakutencall.data.entity.number.TelephoneNumber
import jp.ne.nissing.rakutencall.domain.call.GetUsePackageInfo
import jp.ne.nissing.rakutencall.domain.call.ValidatePhoneNumber
import jp.ne.nissing.rakutencall.utils.commons.BasePresenter
import jp.ne.nissing.rakutencall.utils.commons.BaseView

interface CallContract {

    interface View : BaseView {
        fun loadedValidatePhoneNumber(response: ValidatePhoneNumber.Response)
        fun startPhone(response: GetUsePackageInfo.Response)
    }

    interface Presenter : BasePresenter {
        fun validateTelephoneNumber(originalTelephoneNumber: TelephoneNumber, next: (ValidatePhoneNumber.Response) -> Unit)
        fun loadPhoneApp(next: (GetUsePackageInfo.Response) -> Unit)
    }
}
