package jp.ne.nissing.rakutencall.domain.contacts

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class RemoveIgnoreContact @Inject constructor(
        private val contactsDataSource: ContactsDataSource,
        executionThreads: ExecutionThreads)
    : IoUseCase<RemoveIgnoreContact.Request, RemoveIgnoreContact.Response, Throwable>(executionThreads) {
    override fun execute(requestValue: Request): Observable<Response> {
        return contactsDataSource.removeIgnoreContact(requestValue.contact).map { Response(ContactDto(it, false)) }
    }

    data class Request(val contact: Contact) : UseCase.RequestValue
    data class Response(val contactDto: ContactDto) : UseCase.ResponseValue
}
