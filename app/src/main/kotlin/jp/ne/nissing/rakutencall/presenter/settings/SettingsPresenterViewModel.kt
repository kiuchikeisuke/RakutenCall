package jp.ne.nissing.rakutencall.presenter.settings

import android.arch.lifecycle.ViewModel
import jp.ne.nissing.rakutencall.domain.settings.GetPhoneApps
import javax.inject.Inject

class SettingsPresenterViewModel @Inject constructor(private val getPhoneApps: GetPhoneApps) : ViewModel(), SettingsContract.Presenter {

    override fun loadPhoneAppInfos(next: (GetPhoneApps.Response) -> Unit) {
        getPhoneApps.execute(next)
    }

    override fun onCleared() {
        super.onCleared()
        getPhoneApps.dispose()
    }
}
