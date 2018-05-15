package jp.ne.nissing.rakutencall.domain.call

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.app.PackageInfo
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.OutputOnlyUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class GetUsePackageInfo @Inject constructor(
        private val settingDataSource: SettingDataSource,
        executionThreads: ExecutionThreads)
    : OutputOnlyUseCase<GetUsePackageInfo.Response, Throwable>(executionThreads) {
    override fun execute(): Observable<Response> {
        return settingDataSource.getUseAppPackageInfo().map { Response(it) }
    }

    data class Response(val packageInfo: PackageInfo) : UseCase.ResponseValue
}
