package jp.ne.nissing.rakutencall.data.entity.prefix

import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber

interface Prefix {
    val number: String

    fun startWithPrefix(phoneNumber: PhoneNumber): Boolean = phoneNumber.startWithPrefix(this)

    companion object {
        fun generateEmptyPrefix(): Prefix = EmptyPrefix()
    }
}