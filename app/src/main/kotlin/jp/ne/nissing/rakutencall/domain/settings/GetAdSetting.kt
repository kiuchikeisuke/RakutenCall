package jp.ne.nissing.rakutencall.domain.settings

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.OutputOnlyUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class GetAdSetting @Inject constructor(
        private val settingDataSource: SettingDataSource,
        private val executionThreads: ExecutionThreads)
    : OutputOnlyUseCase<GetAdSetting.Response, Throwable>(executionThreads) {
    override fun execute(): Observable<Response> {
        return settingDataSource.getAdEnable().map { Response(it) }.subscribeOn(executionThreads.ui())
    }

    data class Response(val isEnable: Boolean) : UseCase.ResponseValue
}
