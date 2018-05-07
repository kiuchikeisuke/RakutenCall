package jp.ne.nissing.rakutencall.data.entity.prefix

data class IdentificationPrefix(override val number: String) : Prefix {
    override fun equals(other: Any?): Boolean {
        return if (other is Prefix) {
            other.number == number
        } else super.equals(other)
    }
}