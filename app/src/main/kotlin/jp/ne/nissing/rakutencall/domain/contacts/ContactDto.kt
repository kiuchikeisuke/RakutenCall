package jp.ne.nissing.rakutencall.domain.contacts

import jp.ne.nissing.rakutencall.data.entity.contact.Contact

data class ContactDto(val contact: Contact, var isSelected: Boolean)