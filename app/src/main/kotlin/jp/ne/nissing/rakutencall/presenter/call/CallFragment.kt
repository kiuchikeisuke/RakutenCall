package jp.ne.nissing.rakutencall.presenter.call

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.data.entity.number.TelephoneNumber
import jp.ne.nissing.rakutencall.databinding.FragmentCallBinding
import jp.ne.nissing.rakutencall.domain.call.GetUsePackageInfo
import jp.ne.nissing.rakutencall.domain.call.ValidatePhoneNumber
import jp.ne.nissing.rakutencall.presenter.settings.SettingsActivity
import jp.ne.nissing.rakutencall.utils.di.Injectable
import javax.inject.Inject

class CallFragment : Fragment(), Injectable, CallContract.View {

    override fun validateError(throwable: Throwable, telephoneNumber: TelephoneNumber) {
        if (throwable is IllegalArgumentException) {
            Toast.makeText(context, getString(R.string.call_illegal_telephone_number_toast, telephoneNumber.number), Toast.LENGTH_LONG).show()
        }
    }

    private val loadedValidatePhoneNumber: (ValidatePhoneNumber.Response) -> Unit = ::loadedValidatePhoneNumber
    private val startPhone: (GetUsePackageInfo.Response) -> Unit = ::startPhone
    private lateinit var validatedPhoneNumber: TelephoneNumber

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val presenterVM: CallPresenterViewModel  by lazy {
        // If share ViewModel with other fragments on same Activity, fix 'this' -> 'activity!!'
        ViewModelProviders.of(this, viewModelFactory).get(CallPresenterViewModel::class.java)
    }

    private lateinit var binding: FragmentCallBinding


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val telephoneNumber = TelephoneNumber.decodeToUTF8TelephoneNumber(arguments!!.getString(ORIGINAL_TEL))
        presenterVM.validateTelephoneNumber(telephoneNumber, next = loadedValidatePhoneNumber, error = { validateError(it, telephoneNumber) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCallBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun loadedValidatePhoneNumber(response: ValidatePhoneNumber.Response) {
        validatedPhoneNumber = response.phoneNumber.generateTelephoneNumber()
        presenterVM.loadPhoneApp(next = startPhone)
    }

    override fun startPhone(response: GetUsePackageInfo.Response) {
        val intent = Intent(Intent.ACTION_CALL, validatedPhoneNumber.number.toUri()).apply {
            setClassName(response.packageInfo.packageName, response.packageInfo.activityName)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.call_not_found_app_error_toast, Toast.LENGTH_LONG).show()
            SettingsActivity.launch(context!!)
        }
        activity!!.finish()
    }

    companion object {
        private const val ORIGINAL_TEL = "original_tel"

        fun newInstance(originalTel: String): CallFragment {
            return CallFragment().apply {
                arguments = Bundle().apply {
                    putString(ORIGINAL_TEL, originalTel)
                }

            }
        }
    }
}
