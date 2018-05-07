package jp.ne.nissing.rakutencall.domain.call.constraint

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.IoUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class PrefixEnable @Inject constructor(
        private val settingDataSource: SettingDataSource,
        executionThreads: ExecutionThreads)
    : IoUseCase<PrefixEnable.Request, PrefixEnable.Response, Throwable>(executionThreads) {
    override fun execute(requestValue: Request): Observable<Response> {
        return settingDataSource.getPrefixEnable().map {
            if (it) {
                Response(requestValue.prefix)
            } else {
                Response(Prefix.generateEmptyPrefix())
            }
        }
    }

    data class Request(val prefix: Prefix) : UseCase.RequestValue
    data class Response(val prefix: Prefix) : UseCase.ResponseValue
}
