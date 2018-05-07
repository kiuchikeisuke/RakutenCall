package jp.ne.nissing.rakutencall.utils.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import jp.ne.nissing.rakutencall.BuildConfig
import jp.ne.nissing.rakutencall.MainApplication
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.db.DatabaseManager
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads

/* Module for Application */
@Module
class ApplicationModule {
    @Provides
    fun provideContext(application: MainApplication): Context = application

    @Provides
    fun provideSharedPreference(application: MainApplication): SharedPreferences = application.getSharedPreferences(application.getString(R.string.app_name), Context.MODE_PRIVATE)

    @Provides
    fun provideRealm(application: MainApplication): Realm {

        val builder = RealmConfiguration.Builder().name(APP_KEY).initialData { realm ->
            realm.beginTransaction()
            val contacts = DatabaseManager.getInstance(application).contacts
            while (contacts.moveToNext()) {
                val telNumber = contacts.getString(contacts.getColumnIndex(DatabaseManager.COL_TEL_NUMBER))
                val displayName = contacts.getString(contacts.getColumnIndex(DatabaseManager.COL_DISPLAYNAME))
                val contactId = contacts.getString(contacts.getColumnIndex(DatabaseManager.COL_CONTACTS_ID))
                val contact = realm.createObject(Contact::class.java)
                contact.contactId = contactId
                contact.displayName = displayName
                contact.phoneNumber = PhoneNumber(telNumber)
                realm.insert(contact)
            }
            realm.commitTransaction()
            application.deleteDatabase(DatabaseManager.DATABASE_NAME)

        }
        return if (BuildConfig.DEBUG) {
            Realm.getInstance(builder.deleteRealmIfMigrationNeeded().build())
        } else {
            Realm.getInstance(builder.build())
        }
    }

    @Provides
    fun provideExecutionThreads(): ExecutionThreads {
        return object : ExecutionThreads {
            override fun ui(): Scheduler = AndroidSchedulers.mainThread()
            override fun io(): Scheduler = Schedulers.io()
        }
    }

    companion object {
        private const val APP_KEY = "jp.ne.nissing.rakutencall"
    }
}
