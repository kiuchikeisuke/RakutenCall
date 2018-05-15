package jp.ne.nissing.rakutencall.data.datasource.contacts

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.data.entity.prefix.FreeDialPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.IdentificationPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.InternationalPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.SpecialPrefix

interface ContactsDataSource {
    fun addIgnoreContact(contact: Contact): Observable<Contact>
    fun removeIgnoreContact(contact: Contact): Observable<Contact>
    fun getIgnoreContacts(): Observable<List<Contact>>

    fun getEnableInternationalPrefix(): Observable<List<InternationalPrefix>>
    fun getDisableInternationalPrefix(): Observable<List<InternationalPrefix>>
    fun getInternationalIdentificationPrefix(): Observable<List<IdentificationPrefix>>

    fun getFreeDialPrefix(): Observable<List<FreeDialPrefix>>

    fun getSpecialNumberPrefix(): Observable<List<SpecialPrefix>>
    fun getSpecialNumberLength(): Observable<Int>

    fun getContacts(): Observable<List<Contact>>
}
