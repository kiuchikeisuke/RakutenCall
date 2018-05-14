package jp.ne.nissing.rakutencall.presenter.contacts

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.databinding.FragmentIgnoreContactListBinding
import jp.ne.nissing.rakutencall.domain.contacts.GetContactsInfo
import jp.ne.nissing.rakutencall.utils.di.Injectable
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

@RuntimePermissions
class IgnoreContactListFragment : Fragment(), Injectable, IgnoreContactListContract.View {
    private val loadedContacts: (GetContactsInfo.Response) -> Unit = ::loadedContacts

    override fun loadedContacts(response: GetContactsInfo.Response) {
        if (binding.contactList.adapter == null) {
            binding.contactList.adapter = ContactAdapter(response.contactDto, { position: Int, contact: Contact, isSelected: Boolean ->
                if (isSelected) {
                    presenterVM.addIgnoreContact(contact, {
                        (binding.contactList.adapter as ContactAdapter).entities[position].isSelected = it.contactDto.isSelected
                    })
                } else {
                    presenterVM.removeIgnoreContact(contact, {
                        (binding.contactList.adapter as ContactAdapter).entities[position].isSelected = it.contactDto.isSelected
                    })
                }
            })
        } else {
            (binding.contactList.adapter as ContactAdapter).entities = response.contactDto
            binding.contactList.adapter.notifyDataSetChanged()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val presenterVM: IgnoreContactListPresenterViewModel  by lazy {
        // If share ViewModel with other fragments on same Activity, fix 'this' -> 'activity!!'
        ViewModelProviders.of(this, viewModelFactory).get(IgnoreContactListPresenterViewModel::class.java)
    }

    private lateinit var binding: FragmentIgnoreContactListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIgnoreContactListBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContacts()
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    fun loadContacts() {
        presenterVM.loadContacts(loadedContacts)
    }

    companion object {
        fun newInstance(): IgnoreContactListFragment {
            val args = Bundle()
            val fragment = IgnoreContactListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
