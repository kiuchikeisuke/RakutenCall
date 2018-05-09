package jp.ne.nissing.rakutencall.data.datasource.settings

import android.content.SharedPreferences
import android.os.Build
import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.entity.app.PackageInfo
import jp.ne.nissing.rakutencall.data.entity.prefix.IgnorePrefix
import javax.inject.Inject

class SettingRepository @Inject constructor(private val sharedPreferences: SharedPreferences) : SettingDataSource {
    private val edit: SharedPreferences.Editor by lazy { sharedPreferences.edit() }

    override fun setUseAppPackageInfo(packageInfo: PackageInfo): Observable<Unit> {
        return Observable.create {
            edit.putString(KEY_PHONE_APP_LIST, packageInfo.getUriString())
            edit.apply()
            it.onComplete()
        }
    }

    override fun getUseAppPackageInfo(): Observable<PackageInfo> {
        return Observable.create {
            val defPackageName = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) DEFAULT_VALUE_PACKAGE_L else DEFAULT_VALUE_PACKAGE
            val defActivityName = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) DEFALUT_VALUE_ACTIVITY_L else DEFALUT_VALUE_ACTIVITY
            val defPackageInfo = PackageInfo(defPackageName, defActivityName)
            val packageUriString = sharedPreferences.getString(KEY_PHONE_APP_LIST, defPackageInfo.getUriString())
            it.onNext(PackageInfo.parseUriString(packageUriString))
            it.onComplete()
        }
    }


    override fun setPrefixNumber(prefix: IgnorePrefix): Observable<Unit> {
        return Observable.create {
            edit.putString(KEY_PREFIX_NUM, prefix.number)
            edit.apply()
            it.onComplete()
        }
    }

    override fun getPrefixNumber(): Observable<IgnorePrefix> {
        return Observable.create {
            it.onNext(IgnorePrefix(sharedPreferences.getString(KEY_PREFIX_NUM, DEFAULT_VALUE_PREFIX_NUM)))
            it.onComplete()
        }
    }

    override fun setAdEnable(isEnable: Boolean): Observable<Unit> {
        return Observable.create {
            edit.putBoolean(KEY_ADBUDDIZ, isEnable)
            edit.apply()
            it.onComplete()
        }
    }

    override fun getAdEnable(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPreferences.getBoolean(KEY_ADBUDDIZ, DEFAULT_VALUE_ADBUDDIZ))
            it.onComplete()
        }
    }

    override fun setPrefixEnable(isEnable: Boolean): Observable<Unit> {
        return Observable.create {
            edit.putBoolean(KEY_PREFIX_ENABLE, isEnable)
            edit.apply()
            it.onComplete()
        }

    }

    override fun getPrefixEnable(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPreferences.getBoolean(KEY_PREFIX_ENABLE, DEFAULT_VALUE_PREFIX_ENABLE))
            it.onComplete()
        }
    }

    override fun setFreeDialEnable(isEnable: Boolean): Observable<Unit> {
        return Observable.create {
            edit.putBoolean(KEY_FREE_DIAL, isEnable)
            edit.apply()
            it.onComplete()
        }
    }

    override fun getFreeDialEnable(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPreferences.getBoolean(KEY_FREE_DIAL, DEFAULT_VALUE_FREE_DIAL_IGNORE))
            it.onComplete()
        }

    }

    override fun setSpecialNumIgnoreEnable(isEnable: Boolean): Observable<Unit> {
        return Observable.create {
            edit.putBoolean(KEY_SPECIAL_NUM_IGNORE, isEnable)
            edit.apply()
            it.onComplete()
        }
    }

    override fun getSpecialNumIgnoreEnable(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPreferences.getBoolean(KEY_SPECIAL_NUM_IGNORE, DEFAULT_VALUE_SPECIAL_NUM_IGNORE))
            it.onComplete()
        }

    }

    override fun setInternationalNumEnable(isEnable: Boolean): Observable<Unit> {
        return Observable.create {
            edit.putBoolean(KEY_INTERNATIONAL_NUM, isEnable)
            edit.apply()
            it.onComplete()
        }
    }

    override fun getInternationalNumEnable(): Observable<Boolean> {
        return Observable.create {
            it.onNext(sharedPreferences.getBoolean(KEY_INTERNATIONAL_NUM, DEFAULT_VALUE_INTERNATIONAL_NUM))
            it.onComplete()
        }
    }

    companion object {
        private const val KEY_PHONE_APP_LIST = "phone_app_list"
        private const val KEY_PREFIX_NUM = "prefix_num"
        private const val KEY_FREE_DIAL = "free_dial"
        private const val KEY_ADBUDDIZ = "show_adbuddiz"
        private const val KEY_PREFIX_ENABLE = "prefix_enable"
        private const val KEY_SPECIAL_NUM_IGNORE = "special_num"
        private const val KEY_INTERNATIONAL_NUM = "international_num"

        private const val DEFAULT_VALUE_PREFIX_NUM = "003768"
        private const val DEFAULT_VALUE_FREE_DIAL_IGNORE = true
        private const val DEFAULT_VALUE_ADBUDDIZ = false
        private const val DEFAULT_VALUE_PREFIX_ENABLE = true
        private const val DEFAULT_VALUE_SPECIAL_NUM_IGNORE = true
        private const val DEFAULT_VALUE_INTERNATIONAL_NUM = true

        //AndroidLより前のパッケージ名、アプリ名
        private const val DEFAULT_VALUE_PACKAGE = "com.android.phone"
        private const val DEFALUT_VALUE_ACTIVITY = "com.android.phone.OutgoingCallBroadcaster"
        //AndroidL以降のパッケージ名、アプリ名
        private const val DEFAULT_VALUE_PACKAGE_L = "com.android.server.telecom"
        private const val DEFALUT_VALUE_ACTIVITY_L = "com.android.server.telecom.CallActivity"

    }
}
