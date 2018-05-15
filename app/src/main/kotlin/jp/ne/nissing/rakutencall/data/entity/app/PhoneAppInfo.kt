package jp.ne.nissing.rakutencall.data.entity.app

import android.graphics.drawable.Drawable

data class PhoneAppInfo(val packageInfo: PackageInfo,
                        val label: String,
                        val icon: Drawable)