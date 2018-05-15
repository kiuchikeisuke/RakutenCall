package jp.ne.nissing.rakutencall.presenter.contacts

import android.arch.lifecycle.ViewModel
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.domain.contacts.AddIgnoreContact
import jp.ne.nissing.rakutencall.domain.contacts.GetContactsInfo
import jp.ne.nissing.rakutencall.domain.contacts.RemoveIgnoreContact
import javax.inject.Inject

class IgnoreContactListPresenterViewModel @Inject constructor(
        private val getContactsInfo: GetContactsInfo,
        private val addIgnoreContact: AddIgnoreContact,
        private val removeIgnoreContact: RemoveIgnoreContact) : ViewModel(), IgnoreContactListContract.Presenter {
    override fun addIgnoreContact(contact: Contact, next: (AddIgnoreContact.Response) -> Unit) {
        addIgnoreContact.execute(AddIgnoreContact.Request(contact), next)
    }

    override fun removeIgnoreContact(contact: Contact, next: (RemoveIgnoreContact.Response) -> Unit) {
        removeIgnoreContact.execute(RemoveIgnoreContact.Request(contact), next)
    }

    override fun loadContacts(next: (GetContactsInfo.Response) -> Unit) {
        getContactsInfo.execute(next)
    }

    override fun onCleared() {
        super.onCleared()
        getContactsInfo.dispose()
        addIgnoreContact.dispose()
        removeIgnoreContact.dispose()
    }
}
