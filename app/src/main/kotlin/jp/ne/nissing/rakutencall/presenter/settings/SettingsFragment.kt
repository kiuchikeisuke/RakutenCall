package jp.ne.nissing.rakutencall.presenter.settings

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import com.purplebrain.adbuddiz.sdk.AdBuddiz
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.domain.settings.GetAdSetting
import jp.ne.nissing.rakutencall.domain.settings.GetIgnorePrefix
import jp.ne.nissing.rakutencall.domain.settings.GetPhoneApps
import jp.ne.nissing.rakutencall.utils.commons.Const
import jp.ne.nissing.rakutencall.utils.di.Injectable
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), Injectable, SettingsContract.View {
    private val loadedAdSetting: (GetAdSetting.Response) -> Unit = ::loadedAdSetting
    override fun loadedAdSetting(response: GetAdSetting.Response) {
        if (response.isEnable) {
            AdBuddiz.showAd(activity)
        }
    }

    private val loadedIgnorePrefix: (GetIgnorePrefix.Response) -> Unit = ::loadedIgnorePrefix
    override fun loadedIgnorePrefix(response: GetIgnorePrefix.Response) {
        (findPreference(getString(R.string.key_prefix_num)) as? EditTextPreference)?.apply {
            summary = response.prefix.number
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                summary = newValue.toString()
                true
            }
        }
    }

    private val loadedPhoneAppInfos: (GetPhoneApps.Response) -> Unit = ::loadedPhoneAppInfos
    override fun loadedPhoneAppInfos(response: GetPhoneApps.Response) {
        (findPreference(getString(R.string.key_phone_app_list)) as? ListPreference)?.apply {
            entries = response.phoneAppInfos.map { it.label }.toTypedArray()
            entryValues = response.phoneAppInfos.map { it.packageInfo.getUriString() }.toTypedArray()
            setDefaultValue(response.usePackageInfo.getUriString())
            summary = response.phoneAppInfos.first { it.packageInfo == response.usePackageInfo }.label
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                summary = response.phoneAppInfos.first { it.packageInfo.getUriString() == newValue }.label
                true
            }
        }
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        preferenceManager.sharedPreferencesName = Const.APP_KEY
        addPreferencesFromResource(R.xml.preferences)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val presenterVM: SettingsPresenterViewModel  by lazy {
        // If share ViewModel with other fragments on same Activity, fix 'this' -> 'activity!!'
        ViewModelProviders.of(this, viewModelFactory).get(SettingsPresenterViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenterVM.loadPhoneAppInfos(loadedPhoneAppInfos)
        presenterVM.loadIgnorePrefix(loadedIgnorePrefix)
        presenterVM.loadAdSetting(loadedAdSetting)
        AdBuddiz.cacheAds(activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        AdBuddiz.onDestroy()
    }

    companion object {
        fun newInstance(): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
