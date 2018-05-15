package jp.ne.nissing.rakutencall.data.entity.contact

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber

open class Contact : RealmObject() {
    open lateinit var displayName: String
    open lateinit var contactId: String
    @PrimaryKey
    open var phoneNumberString: String = ""

    fun getPhoneNumber() = PhoneNumber(phoneNumberString)

    override fun equals(other: Any?): Boolean {
        return if (other is Contact) {
            this.phoneNumberString == other.phoneNumberString
        } else {
            super.equals(other)
        }
    }
}