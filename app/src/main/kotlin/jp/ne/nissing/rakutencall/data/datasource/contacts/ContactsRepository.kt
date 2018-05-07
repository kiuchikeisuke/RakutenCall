package jp.ne.nissing.rakutencall.data.datasource.contacts

import android.content.Context
import android.provider.ContactsContract
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.realm.Realm
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.data.entity.prefix.FreeDialPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.IdentificationPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.InternationalPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.SpecialPrefix
import jp.ne.nissing.rakutencall.utils.extensions.subscribeOnMainThread
import javax.inject.Inject

class ContactsRepository @Inject constructor(private val realm: Realm, private val context: Context) : ContactsDataSource {
    override fun setIgnoreContact(contact: Contact): Observable<Unit> {
        return Observable.create { e: ObservableEmitter<Unit> ->
            realm.beginTransaction()
            realm.insertOrUpdate(realm.copyToRealm(contact))
            e.onNext(realm.commitTransaction())
            e.onComplete()
        }.subscribeOnMainThread()
    }

    override fun deleteIgnoreContact(contact: Contact): Observable<Unit> {
        return Observable.create { e: ObservableEmitter<Unit> ->
            val results = realm.where(Contact::class.java).equalTo("phoneNumberString", contact.phoneNumberString).findAll()
            realm.beginTransaction()
            results.deleteAllFromRealm()
            e.onNext(realm.commitTransaction())
            e.onComplete()
        }.subscribeOnMainThread()
    }

    override fun getIgnoreContacts(): Observable<List<Contact>> {
        return Observable.create { e: ObservableEmitter<List<Contact>> ->
            e.onNext(realm.where(Contact::class.java).findAll().toList())
            e.onComplete()
        }.subscribeOnMainThread()
    }

    override fun getEnableInternationalPrefix(): Observable<List<InternationalPrefix>> {
        return Observable.create {
            it.onNext(context.resources.getStringArray(R.array.international_enable_prefixes).map { InternationalPrefix(it) })
            it.onComplete()
        }
    }

    override fun getDisableInternationalPrefix(): Observable<List<InternationalPrefix>> {
        return Observable.create {
            it.onNext(context.resources.getStringArray(R.array.international_disable_prefixes).map { InternationalPrefix(it) })
            it.onComplete()
        }
    }

    override fun getInternationalIdentificationPrefix(): Observable<List<IdentificationPrefix>> {
        return Observable.create {
            it.onNext(context.resources.getStringArray(R.array.identification_prefixes).map { IdentificationPrefix(it) })
            it.onComplete()
        }
    }

    override fun getFreeDialPrefix(): Observable<List<FreeDialPrefix>> {
        return Observable.create {
            it.onNext(context.resources.getStringArray(R.array.free_dial_prefixes).map { FreeDialPrefix(it) })
            it.onComplete()
        }
    }

    override fun getSpecialNumberPrefix(): Observable<List<SpecialPrefix>> {
        return Observable.create {
            it.onNext(context.resources.getStringArray(R.array.special_prefixes).map { SpecialPrefix(it) })
            it.onComplete()
        }
    }

    override fun getSpecialNumberLength(): Observable<Int> {
        return Observable.create {
            it.onNext(SPECIAL_NUMBER_MAX_LENGTH)
            it.onComplete()
        }
    }

    override fun getContacts(): Observable<List<Contact>> {
        return Observable.create {
            val contentResolver = context.contentResolver

            val phoneCursor = contentResolver
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            arrayOf(ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DATA1),
                            ContactsContract.Data.MIMETYPE + " = ?",
                            arrayOf(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE), null)
            val tempHash = HashMap<String, String>()

            while (phoneCursor.moveToNext()) {
                val id = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID))
                val number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Data.DATA1))

                if (id != null && number != null) {
                    tempHash[id] = number
                }
            }
            phoneCursor.close()

            val structuredNameCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                    arrayOf(ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME),
                    ContactsContract.Data.MIMETYPE + " = ?",
                    arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE), CONTACT_SORT_ORDER)

            val contacts = ArrayList<Contact>()
            while (structuredNameCursor.moveToNext()) {
                val id = structuredNameCursor.getString(structuredNameCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID))
                val displayName = structuredNameCursor.getString(structuredNameCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))

                if (id != null) {
                    tempHash[id]?.let {
                        val contact = Contact()
                        contact.phoneNumberString = it
                        contact.displayName = displayName
                        contact.contactId = id
                        contacts.add(contact)
                    }
                }
            }
            structuredNameCursor.close()

            it.onNext(contacts)
            it.onComplete()
        }
    }


    companion object {
        private const val SPECIAL_NUMBER_MAX_LENGTH = 5
        private const val CONTACT_SORT_ORDER = (" CASE" + " WHEN IFNULL(" + ContactsContract.Data.DATA9 + ", '') = ''"
                + // Data.DATA9がNULLの場合は空文字を代入
                " THEN 1 ELSE 0"
                + // Data.DATA9が空文字のレコードを最後にする
                " END, " + ContactsContract.Data.DATA9 + " ," + " CASE" + " WHEN IFNULL(" + ContactsContract.Data.DATA7
                + ", '') = ''" + " THEN 1 ELSE 0" + " END, " + ContactsContract.Data.DATA7)
    }
}
