package jp.ne.nissing.rakutencall.utils.di

import dagger.Module

/* module for Repositories */
@Module
abstract class RepositoryModules {
    /*if you add a new datasource & repository, add a new provideMethod. Like this */
//  @Binds abstract fun bindSomeDataSource(repository: SomeRepository): SomeDataSource
}
