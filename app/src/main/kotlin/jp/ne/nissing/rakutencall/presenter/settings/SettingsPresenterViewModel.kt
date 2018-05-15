package jp.ne.nissing.rakutencall.presenter.settings

import android.arch.lifecycle.ViewModel
import jp.ne.nissing.rakutencall.domain.settings.GetAdSetting
import jp.ne.nissing.rakutencall.domain.settings.GetIgnorePrefix
import jp.ne.nissing.rakutencall.domain.settings.GetPhoneApps
import javax.inject.Inject

class SettingsPresenterViewModel @Inject constructor(
        private val getPhoneApps: GetPhoneApps,
        private val getIgnorePrefix: GetIgnorePrefix,
        private val getAdSetting: GetAdSetting
) : ViewModel(), SettingsContract.Presenter {

    override fun loadAdSetting(next: (GetAdSetting.Response) -> Unit) {
        getAdSetting.execute(next)
    }

    override fun loadIgnorePrefix(next: (GetIgnorePrefix.Response) -> Unit) {
        getIgnorePrefix.execute(next)
    }

    override fun loadPhoneAppInfos(next: (GetPhoneApps.Response) -> Unit) {
        getPhoneApps.execute(next)
    }

    override fun onCleared() {
        super.onCleared()
        getPhoneApps.dispose()
        getIgnorePrefix.dispose()
        getAdSetting.dispose()
    }
}
