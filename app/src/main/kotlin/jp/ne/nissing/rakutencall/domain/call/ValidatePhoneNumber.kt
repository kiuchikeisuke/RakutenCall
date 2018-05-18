package jp.ne.nissing.rakutencall.domain.call

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.domain.call.constraint.*
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class ValidatePhoneNumber @Inject constructor(
        private val settingDataSource: SettingDataSource,
        private val prefixEnable: PrefixEnable,
        private val freeDial: FreeDial,
        private val ignoreNumber: IgnoreNumber,
        private val internationalNumber: InternationalNumber,
        private val specialNumber: SpecialNumber,
        private val specialLength: SpecialLength,
        private val startWithPrefix: StartWithPrefix,
        private val executionThreads: ExecutionThreads)
    : IoUseCase<ValidatePhoneNumber.Request, ValidatePhoneNumber.Response, Throwable>(executionThreads) {

    override fun execute(requestValue: Request): Observable<Response> {
        return settingDataSource.getPrefixNumber().flatMap {
            prefixEnable.execute(PrefixEnable.Request(it))
        }.flatMap {
            freeDial.execute(FreeDial.Request(it.prefix, requestValue.phoneNumber))
        }.flatMap {
            ignoreNumber.execute(IgnoreNumber.Request(it.prefix, requestValue.phoneNumber))
        }.flatMap {
            internationalNumber.execute(InternationalNumber.Request(it.prefix, requestValue.phoneNumber))
        }.flatMap {
            specialNumber.execute(SpecialNumber.Request(it.prefix, requestValue.phoneNumber))
        }.flatMap {
            specialLength.execute(SpecialLength.Request(it.prefix, requestValue.phoneNumber))
        }.flatMap {
            startWithPrefix.execute(StartWithPrefix.Request(it.prefix, requestValue.phoneNumber))
        }.map {
            Response(requestValue.phoneNumber.addPrefix(it.prefix))
        }.subscribeOn(executionThreads.ui())
    }

    data class Request(val phoneNumber: PhoneNumber) : UseCase.RequestValue
    data class Response(val phoneNumber: PhoneNumber) : UseCase.ResponseValue
}
