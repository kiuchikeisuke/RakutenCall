package jp.ne.nissing.rakutencall.domain.call.constraint

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class StartWithPrefix @Inject constructor(
        private val settingDataSource: SettingDataSource,
        executionThreads: ExecutionThreads)
    : IoUseCase<StartWithPrefix.Request, StartWithPrefix.Response, Throwable>(executionThreads) {
    override fun execute(requestValue: Request): Observable<Response> {
        return settingDataSource.getPrefixNumber().map {
            if (it.startWithPrefix(requestValue.phoneNumber))
                Response(Prefix.generateEmptyPrefix())
            else
                Response(requestValue.prefix)
        }
    }

    data class Request(val prefix: Prefix, val phoneNumber: PhoneNumber) : UseCase.RequestValue
    data class Response(val prefix: Prefix) : UseCase.ResponseValue
}
