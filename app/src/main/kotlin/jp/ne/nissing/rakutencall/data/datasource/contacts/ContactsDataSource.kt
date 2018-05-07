package jp.ne.nissing.rakutencall.data.datasource.contacts

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.data.entity.prefix.FreeDialPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.IdentificationPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.InternationalPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.SpecialPrefix

interface ContactsDataSource {
    fun setIgnoreContact(contact: Contact): Observable<Unit>
    fun deleteIgnoreContact(contact: Contact): Observable<Unit>
    fun getIgnoreContacts(): Observable<List<Contact>>

    fun getEnableInternationalPrefix(): Observable<List<InternationalPrefix>>
    fun getDisableInternationalPrefix(): Observable<List<InternationalPrefix>>
    fun getInternationalIdentificationPrefix(): Observable<List<IdentificationPrefix>>

    fun getFreeDialPrefix(): Observable<List<FreeDialPrefix>>

    fun getSpecialNumberPrefix(): Observable<List<SpecialPrefix>>
    fun getSpecialNumberLength(): Observable<Int>

    fun getContacts(): Observable<List<Contact>>
}
