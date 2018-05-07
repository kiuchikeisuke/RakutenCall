package jp.ne.nissing.rakutencall.domain.settings

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import jp.ne.nissing.rakutencall.utils.commons.SimpleUseCase
import javax.inject.Inject

class Initialize @Inject constructor(
        private val settingDataSource: SettingDataSource,
        executionThreads: ExecutionThreads)
    : SimpleUseCase<Throwable>(executionThreads) {
    override fun execute(): Observable<NoResponseValue> {
        return settingDataSource.initPreference().map { NoResponseValue.INSTANCE }
    }

}
