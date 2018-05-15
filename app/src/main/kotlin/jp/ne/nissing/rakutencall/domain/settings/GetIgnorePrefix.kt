package jp.ne.nissing.rakutencall.domain.settings

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.prefix.IgnorePrefix
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.OutputOnlyUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class GetIgnorePrefix @Inject constructor(
        private val settingDataSource: SettingDataSource,
        executionThreads: ExecutionThreads)
    : OutputOnlyUseCase<GetIgnorePrefix.Response, Throwable>(executionThreads) {
    override fun execute(): Observable<Response> {
        return settingDataSource.getPrefixNumber().map { Response(it) }
    }

    data class Response(val prefix: IgnorePrefix) : UseCase.ResponseValue
}
