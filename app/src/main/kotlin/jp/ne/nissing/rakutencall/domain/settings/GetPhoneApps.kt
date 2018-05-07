package jp.ne.nissing.rakutencall.domain.settings

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import jp.ne.nissing.rakutencall.data.datasource.phone.PhoneAppDataSource
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.app.PackageInfo
import jp.ne.nissing.rakutencall.data.entity.app.PhoneAppInfo
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.OutputOnlyUseCase
import jp.ne.nissing.rakutencall.utils.commons.UseCase
import javax.inject.Inject

class GetPhoneApps @Inject constructor(
        private val phoneAppDataSource: PhoneAppDataSource,
        private val settingDataSource: SettingDataSource,
        executionThreads: ExecutionThreads)
    : OutputOnlyUseCase<GetPhoneApps.Response, Throwable>(executionThreads) {
    override fun execute(): Observable<Response> {
        return Observable.zip(phoneAppDataSource.getPhoneAppInfos(),
                settingDataSource.getUseAppPackageInfo(),
                BiFunction { t1, t2 ->
                    Response(t1, t2)
                })
    }

    data class Response(val phoneAppInfos: List<PhoneAppInfo>, val usePackageInfo: PackageInfo) : UseCase.ResponseValue

}
