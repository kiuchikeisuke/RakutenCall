package jp.ne.nissing.rakutencall.domain.call.constraint

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class IgnoreNumber @Inject constructor(
        private val contactsDataSource: ContactsDataSource,
        executionThreads: ExecutionThreads)
    : IoUseCase<IgnoreNumber.Request, IgnoreNumber.Response, Throwable>(executionThreads) {

    override fun execute(requestValue: Request): Observable<Response> {
        return contactsDataSource.getIgnoreNumbers().map {
            if (it.any { it == requestValue.phoneNumber })
                Response(Prefix.generateEmptyPrefix())
            else
                Response(requestValue.prefix)
        }
    }

    data class Request(val prefix: Prefix, val phoneNumber: PhoneNumber) : UseCase.RequestValue
    data class Response(val prefix: Prefix) : UseCase.ResponseValue
}
