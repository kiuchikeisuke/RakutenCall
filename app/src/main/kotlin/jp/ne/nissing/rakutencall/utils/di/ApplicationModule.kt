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
import jp.ne.nissing.rakutencall.utils.commons.Const
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads

/* Module for Application */
@Module
class ApplicationModule {
    @Provides
    fun provideContext(application: MainApplication): Context = application

    @Provides
    fun provideSharedPreference(application: MainApplication): SharedPreferences = application.getSharedPreferences(Const.APP_KEY, Context.MODE_PRIVATE)

    @Provides
    fun provideRealm(transaction: Realm.Transaction): Realm {
        val builder = RealmConfiguration.Builder().name(Const.APP_KEY)
        builder.initialData(transaction).schemaVersion(Const.REALM_SCHEMA_VERSION)
        return if (BuildConfig.BUILD_TYPE == "debug") {
            Realm.getInstance(builder.deleteRealmIfMigrationNeeded().build())
        } else {
            Realm.getInstance(builder.build())
        }
    }

    @Provides
    fun provideTransaction(application: MainApplication) = application.transaction

    @Provides
    fun provideExecutionThreads(): ExecutionThreads {
        return object : ExecutionThreads {
            override fun ui(): Scheduler = AndroidSchedulers.mainThread()
            override fun io(): Scheduler = Schedulers.io()
        }
    }
}
