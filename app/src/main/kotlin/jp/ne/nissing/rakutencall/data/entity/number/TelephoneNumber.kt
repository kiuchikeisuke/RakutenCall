package jp.ne.nissing.rakutencall.data.entity.number

import java.net.URLDecoder

data class TelephoneNumber(val number: String) {
    init {
        if (!number.startsWith(SCHEME)) {
            throw IllegalArgumentException("TelephoneNumber  must start with 'tel:'")
        } else if (number.substring(SCHEME.length).contains(Regex(".*[^0-9#¥+¥*() -]+.*"))) {
            throw IllegalArgumentException("contains illegal char. number = $number")
        }
    }

    fun generatePhoneNumber() = PhoneNumber(number.substring(SCHEME.length))

    companion object {
        const val SCHEME = "tel:"
        fun decodeToUTF8TelephoneNumber(number: String) = TelephoneNumber(URLDecoder.decode(number, "UTF-8"))
    }
}