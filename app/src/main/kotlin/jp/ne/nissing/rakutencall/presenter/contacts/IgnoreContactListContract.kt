package jp.ne.nissing.rakutencall.presenter.contacts

import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.domain.contacts.AddIgnoreContact
import jp.ne.nissing.rakutencall.domain.contacts.GetContactsInfo
import jp.ne.nissing.rakutencall.domain.contacts.RemoveIgnoreContact
import jp.ne.nissing.rakutencall.utils.commons.BasePresenter
import jp.ne.nissing.rakutencall.utils.commons.BaseView

interface IgnoreContactListContract {

    interface View : BaseView {
        fun loadedContacts(response: GetContactsInfo.Response)
    }

    interface Presenter : BasePresenter {
        fun loadContacts(next: (GetContactsInfo.Response) -> Unit)
        fun addIgnoreContact(contact: Contact, next: (AddIgnoreContact.Response) -> Unit)
        fun removeIgnoreContact(contact: Contact, next: (RemoveIgnoreContact.Response) -> Unit)
    }
}
