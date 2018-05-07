package jp.ne.nissing.rakutencall.data.entity.contact

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber

open class Contact : RealmObject() {
    @Ignore
    open lateinit var phoneNumber: PhoneNumber
    open lateinit var displayName: String
    open lateinit var contactId: String
    @PrimaryKey
    open var phoneNumberString: String = phoneNumber.number
}