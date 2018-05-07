package jp.ne.nissing.rakutencall.domain.call.constraint

import io.reactivex.Observable
import io.reactivex.functions.Function4
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.data.entity.prefix.IdentificationPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.InternationalPrefix
import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class InternationalNumber @Inject constructor(
        private val settingDataSource: SettingDataSource,
        private val contactsDataSource: ContactsDataSource,
        executionThreads: ExecutionThreads)
    : IoUseCase<InternationalNumber.Request, InternationalNumber.Response, Throwable>(executionThreads) {
    override fun execute(requestValue: Request): Observable<Response> {
        return Observable
                .zip(settingDataSource.getInternationalNumEnable(),
                        contactsDataSource.getInternationalIdentificationPrefix(),
                        contactsDataSource.getEnableInternationalPrefix(),
                        contactsDataSource.getDisableInternationalPrefix(),
                        Function4 { t1, t2, t3, t4 ->
                            if (!t1 || t2.none { it.startWithPrefix(requestValue.phoneNumber) })
                                Response(requestValue.prefix)
                            else {
                                validateInternationalPrefix(t2, requestValue, t3, t4)
                            }
                        })
    }

    private fun validateInternationalPrefix(t2: List<IdentificationPrefix>, requestValue: Request, t3: List<InternationalPrefix>, t4: List<InternationalPrefix>): Response {
        val identificationPrefix = t2.find { it.startWithPrefix(requestValue.phoneNumber) }
        return if (identificationPrefix == null) {
            Response(requestValue.prefix)
        } else {
            if (t3.none { it.startWithPrefix(requestValue.phoneNumber.removePrefix(identificationPrefix)) }
                    || t4.any { it.startWithPrefix(requestValue.phoneNumber.removePrefix(identificationPrefix)) }) {
                Response(Prefix.generateEmptyPrefix())
            } else {
                Response(requestValue.prefix)
            }
        }
    }

    data class Request(val prefix: Prefix, val phoneNumber: PhoneNumber) : UseCase.RequestValue
    data class Response(val prefix: Prefix) : UseCase.ResponseValue
}
