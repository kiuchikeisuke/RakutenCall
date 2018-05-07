package jp.ne.nissing.rakutencall.data.datasource.phone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri
import io.reactivex.Observable
import jp.ne.nissing.rakutencall.data.entity.app.PackageInfo
import jp.ne.nissing.rakutencall.data.entity.app.PhoneAppInfo
import jp.ne.nissing.rakutencall.data.entity.number.TelephoneNumber
import javax.inject.Inject

class PhoneAppRepository @Inject constructor(private val context: Context) : PhoneAppDataSource {
    override fun getPhoneAppInfos(): Observable<List<PhoneAppInfo>> {
        return Observable.create { emitter ->
            val pm = context.packageManager
            val intent = Intent(Intent.ACTION_CALL, TelephoneNumber.SCHEME.toUri()).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            emitter.onNext(pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    .filter { it.activityInfo.packageName != context.packageName }
                    .map {
                        PhoneAppInfo(PackageInfo(it.activityInfo.packageName, it.activityInfo.name),
                                it.loadLabel(pm).toString(),
                                it.loadIcon(pm))
                    }
            )
            emitter.onComplete()
        }
    }
}
