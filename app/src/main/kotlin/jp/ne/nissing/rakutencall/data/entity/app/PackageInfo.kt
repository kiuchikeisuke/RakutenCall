package jp.ne.nissing.rakutencall.data.entity.app

data class PackageInfo(val packageName: String, val activityName: String) {
    fun getUriString() = "$packageName#$activityName"

    companion object {
        fun parseUriString(string: String): PackageInfo =
                PackageInfo(string.split("#")[0], string.split("#")[1])
    }
}