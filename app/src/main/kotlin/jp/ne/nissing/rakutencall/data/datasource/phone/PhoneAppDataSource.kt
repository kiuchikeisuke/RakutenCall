package jp.ne.nissing.rakutencall.data.datasource.phone

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.entity.app.PhoneAppInfo

interface PhoneAppDataSource {
    fun getPhoneAppInfos(): Observable<List<PhoneAppInfo>>
}
