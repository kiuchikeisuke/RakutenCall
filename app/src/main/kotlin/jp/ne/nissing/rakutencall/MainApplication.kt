package jp.ne.nissing.rakutencall

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.realm.Realm
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.db.DatabaseManager
import jp.ne.nissing.rakutencall.utils.di.DaggerRootComponent
import jp.ne.nissing.rakutencall.utils.di.applyAutoInjector
import timber.log.Timber

class MainApplication : DaggerApplication() {
    val transaction: Realm.Transaction by lazy {
        Realm.Transaction {
            val dbm = DatabaseManager.getInstance(this)
            dbm.open()
            val contacts = dbm.contacts
            while (contacts.moveToNext()) {
                val telNumber = contacts.getString(contacts.getColumnIndex(DatabaseManager.COL_TEL_NUMBER))
                val displayName = contacts.getString(contacts.getColumnIndex(DatabaseManager.COL_DISPLAYNAME))
                val contactId = contacts.getString(contacts.getColumnIndex(DatabaseManager.COL_CONTACTS_ID))
                val contact = it.createObject(Contact::class.java, telNumber)
                contact.contactId = contactId
                contact.displayName = displayName
            }
            dbm.close()
            this.deleteDatabase(DatabaseManager.DATABASE_NAME)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerRootComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Realm.init(this)
        applyAutoInjector()
    }
}
