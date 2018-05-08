package jp.ne.nissing.rakutencall.domain.contacts

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.OutputOnlyUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class GetContactsInfo @Inject constructor(
        private val contactsDataSource: ContactsDataSource,
        executionThreads: ExecutionThreads)
    : OutputOnlyUseCase<GetContactsInfo.Response, Throwable>(executionThreads) {
    override fun execute(): Observable<Response> {
        return Observable.zip(contactsDataSource.getContacts(), contactsDataSource.getIgnoreContacts(),
                BiFunction { t1, t2 ->
                    Response(t1.map { contact ->
                        if (t2.any { contact == it }) {
                            ContactDto(contact, true)
                        } else {
                            ContactDto(contact, false)
                        }
                    })
                })
    }

    data class Response(val contactDto: List<ContactDto>) : UseCase.ResponseValue
}
