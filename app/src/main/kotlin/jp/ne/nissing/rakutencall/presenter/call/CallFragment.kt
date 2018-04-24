package jp.ne.nissing.rakutencall.presenter.call

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.fragment_call.*
import javax.inject.Inject
import jp.ne.nissing.rakutencall.debug.R
import jp.ne.nissing.rakutencall.debug.databinding.FragmentCallBinding
import jp.ne.nissing.rakutencall.debug.utils.di.Injectable

class CallFragment : Fragment(), Injectable, CallContract.View {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val presenterVM:CallPresenterViewModel  by lazy {
        // If share ViewModel with other fragments on same Activity, fix 'this' -> 'activity!!'
        ViewModelProviders.of(this, viewModelFactory).get(CallPresenterViewModel::class.java)
    }

    private lateinit var binding: FragmentCallBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCallBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    companion object {
        fun newInstance(): CallFragment {
            val args = Bundle()
            val fragment = CallFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
