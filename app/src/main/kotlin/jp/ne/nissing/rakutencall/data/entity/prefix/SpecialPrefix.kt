package jp.ne.nissing.rakutencall.data.entity.prefix

data class SpecialPrefix(override val number: String) : Prefix {
    override fun equals(other: Any?): Boolean {
        return if (other is Prefix) {
            other.number == number
        } else super.equals(other)
    }
}