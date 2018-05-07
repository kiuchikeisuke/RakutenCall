package jp.ne.nissing.rakutencall.presenter.settings

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.databinding.FragmentSettingsBinding
import jp.ne.nissing.rakutencall.domain.settings.GetPhoneApps
import jp.ne.nissing.rakutencall.utils.di.Injectable
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), Injectable, SettingsContract.View {
    private val loadedPhoneAppInfos: (GetPhoneApps.Response) -> Unit = ::loadedPhoneAppInfos

    override fun loadedPhoneAppInfos(response: GetPhoneApps.Response) {
        (findPreference(getString(R.string.key_phone_app_list)) as? ListPreference)?.apply {
            entries = response.phoneAppInfos.map { it.label }.toTypedArray()
            entryValues = response.phoneAppInfos.map { it.packageInfo.getUriString() }.toTypedArray()
            this.setDefaultValue(response.usePackageInfo.getUriString())
        }
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val presenterVM: SettingsPresenterViewModel  by lazy {
        // If share ViewModel with other fragments on same Activity, fix 'this' -> 'activity!!'
        ViewModelProviders.of(this, viewModelFactory).get(SettingsPresenterViewModel::class.java)
    }

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenterVM.loadPhoneAppInfos(loadedPhoneAppInfos)
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
