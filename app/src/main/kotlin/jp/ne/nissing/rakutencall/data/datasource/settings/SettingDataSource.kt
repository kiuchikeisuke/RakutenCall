package jp.ne.nissing.rakutencall.data.datasource.settings

import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.entity.app.PackageInfo
import jp.ne.nissing.rakutencall.data.entity.prefix.IgnorePrefix

interface SettingDataSource {
    fun initPreference(): Observable<Unit>

    fun setUseAppPackageInfo(packageInfo: PackageInfo): Observable<Unit>
    fun getUseAppPackageInfo(): Observable<PackageInfo>

    fun setPrefixNumber(prefix: IgnorePrefix): Observable<Unit>
    fun getPrefixNumber(): Observable<IgnorePrefix>

    fun setAdEnable(isEnable: Boolean): Observable<Unit>
    fun getAdEnable(): Observable<Boolean>

    fun setPrefixEnable(isEnable: Boolean): Observable<Unit>
    fun getPrefixEnable(): Observable<Boolean>

    fun setFreeDialEnable(isEnable: Boolean): Observable<Unit>
    fun getFreeDialEnable(): Observable<Boolean>

    fun setSpecialNumIgnoreEnable(isEnable: Boolean): Observable<Unit>
    fun getSpecialNumIgnoreEnable(): Observable<Boolean>

    fun setInternationalNumEnable(isEnable: Boolean): Observable<Unit>
    fun getInternationalNumEnable(): Observable<Boolean>
}
