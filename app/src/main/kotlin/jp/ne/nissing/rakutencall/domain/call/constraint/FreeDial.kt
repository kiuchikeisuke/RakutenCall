package jp.ne.nissing.rakutencall.domain.call.constraint

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class FreeDial @Inject constructor(
        private val settingDataSource: SettingDataSource,
        private val contactsDataSource: ContactsDataSource,
        executionThreads: ExecutionThreads)
    : IoUseCase<FreeDial.Request, FreeDial.Response, Throwable>(executionThreads) {
    override fun execute(requestValue: Request): Observable<Response> {
        return settingDataSource.getFreeDialEnable()
                .flatMap {
                    if (it)
                        validateFreeDial(requestValue)
                    else
                        Observable.just(Response(requestValue.prefix))
                }
    }

    private fun validateFreeDial(requestValue: Request): Observable<Response> {
        return contactsDataSource.getFreeDialPrefix().map {
            if (it.any { it.startWithPrefix(requestValue.phoneNumber) }) {
                Response(Prefix.generateEmptyPrefix())
            } else {
                Response(requestValue.prefix)
            }
        }.defaultIfEmpty(Response(requestValue.prefix))
    }

    data class Request(val prefix: Prefix, val phoneNumber: PhoneNumber) : UseCase.RequestValue
    data class Response(val prefix: Prefix) : UseCase.ResponseValue
}
