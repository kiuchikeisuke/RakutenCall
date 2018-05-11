package jp.ne.nissing.rakutencall.data.entity.number

import jp.ne.nissing.rakutencall.data.entity.prefix.Prefix

class PhoneNumber(val number: String) {
    init {
        if (number.startsWith(TelephoneNumber.SCHEME)) {
            throw IllegalArgumentException("PhoneNumber does not start with 'tel:'")
        } else if (number.isEmpty()) {
            throw IllegalArgumentException("number must not be Empty")
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
}