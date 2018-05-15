package jp.ne.nissing.rakutencall.data.entity.prefix

class EmptyPrefix : Prefix {
    override val number: String get() = ""

    override fun equals(other: Any?): Boolean {
        return if (other is Prefix) {
            other.number == number
        } else super.equals(other)
    }
}