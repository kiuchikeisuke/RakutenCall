package jp.ne.nissing.rakutencall.data.entity.prefix

import io.realm.annotations.PrimaryKey

data class FreeDialPrefix(
        @PrimaryKey
        override val number: String) : Prefix {
    override fun equals(other: Any?): Boolean {
        return if (other is Prefix) {
            other.number == number
        } else super.equals(other)
    }
}