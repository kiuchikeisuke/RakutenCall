package jp.ne.nissing.rakutencall.utils.di

import dagger.Binds
import dagger.Module
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsRepository
import jp.ne.nissing.rakutencall.data.datasource.phone.PhoneAppDataSource
import jp.ne.nissing.rakutencall.data.datasource.phone.PhoneAppRepository
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingRepository

/* module for Repositories */
@Module
abstract class RepositoryModules {
    /*if you add a new datasource & repository, add a new provideMethod. Like this */
//  @Binds abstract fun bindSomeDataSource(repository: SomeRepository): SomeDataSource
    @Binds
    abstract fun bindContactsDataSource(repository: ContactsRepository): ContactsDataSource

    @Binds
    abstract fun bindPhoneAppDataSource(repository: PhoneAppRepository): PhoneAppDataSource

    @Binds
    abstract fun bindSettingDataSource(repository: SettingRepository): SettingDataSource
}
