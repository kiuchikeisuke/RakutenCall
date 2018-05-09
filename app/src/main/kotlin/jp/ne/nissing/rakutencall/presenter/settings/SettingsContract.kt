package jp.ne.nissing.rakutencall.presenter.settings

import jp.ne.nissing.rakutencall.domain.settings.GetAdSetting
import jp.ne.nissing.rakutencall.domain.settings.GetIgnorePrefix
import jp.ne.nissing.rakutencall.domain.settings.GetPhoneApps
import jp.ne.nissing.rakutencall.utils.commons.BasePresenter
import jp.ne.nissing.rakutencall.utils.commons.BaseView

interface SettingsContract {

    interface View : BaseView {
        fun loadedPhoneAppInfos(response: GetPhoneApps.Response)
        fun loadedIgnorePrefix(response: GetIgnorePrefix.Response)
        fun loadedAdSetting(response: GetAdSetting.Response)
    }

    interface Presenter : BasePresenter {
        fun loadPhoneAppInfos(next: (GetPhoneApps.Response) -> Unit)
        fun loadIgnorePrefix(next: (GetIgnorePrefix.Response) -> Unit)
        fun loadAdSetting(next: (GetAdSetting.Response) -> Unit)
    }
}
