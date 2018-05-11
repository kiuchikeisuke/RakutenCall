package jp.ne.nissing.rakutencall.data.entity.number

import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix
import java.net.URLDecoder

class PhoneNumber(val number: String) {
    init {
        if (number.startsWith(TelephoneNumber.SCHEME)) {
            throw IllegalArgumentException("PhoneNumber does not start with 'tel:'")
        } else if (number.isEmpty()) {
            throw IllegalArgumentException("number must not be Empty")
        } else if ((number.contains(Regex(".*[^0-9#¥+¥*() -]+.*")))) {
            throw IllegalArgumentException("contains illegal char. number = $number")
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is PhoneNumber) {
            other.number == number
        } else
            return super.equals(other)
    }

    fun addPrefix(prefix: Prefix): PhoneNumber = PhoneNumber(prefix.number + number)
    fun removePrefix(prefix: Prefix): PhoneNumber {
        return if (this.startWithPrefix(prefix)) {
            PhoneNumber(number.substring(prefix.number.length))
        } else {
            PhoneNumber(number)
        }
    }

    fun startWithPrefix(prefix: Prefix): Boolean = number.startsWith(prefix.number)
    fun generateTelephoneNumber() = TelephoneNumber(TelephoneNumber.SCHEME + number)

    companion object {
        fun decodeToUTF8PhoneNumber(number: String) = PhoneNumber(URLDecoder.decode(number, "UTF-8"))
    }
}