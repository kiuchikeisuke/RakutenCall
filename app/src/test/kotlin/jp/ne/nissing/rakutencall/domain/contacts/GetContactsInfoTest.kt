package jp.ne.nissing.rakutencall.domain.contacts

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.utils.commons.ExecutionThreads
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(JUnitPlatform::class)
object GetContactsInfoTest : Spek({
    val executionThreads: ExecutionThreads = object : ExecutionThreads {
        override fun io(): Scheduler = Schedulers.trampoline()
        override fun ui(): Scheduler = Schedulers.trampoline()
    }
    context("getContactsInfo Test") {
        given("base contacts exist, ignore contacts empty") {
            val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
            Mockito.`when`(contactsDataSource.getContacts()).then {
                Observable.create<List<Contact>> {
                    val c1 = Contact().apply {
                        displayName = "あいうえお"
                        contactId = "12345"
                        phoneNumberString = "1234567890"
                    }
                    val c2 = Contact().apply {
                        displayName = "かきくけこ"
                        contactId = "22222"
                        phoneNumberString = "2222222222"
                    }
                    val c3 = Contact().apply {
                        displayName = "さしすせそ"
                        contactId = "33333"
                        phoneNumberString = "3333333333"
                    }
                    val list = listOf(c1, c2, c3)
                    it.onNext(list)
                    it.onComplete()
                }
            }
            Mockito.`when`(contactsDataSource.getIgnoreContacts()).then {
                Observable.just<List<Contact>>(listOf())
            }
            val getContactsInfo = GetContactsInfo(contactsDataSource, executionThreads)

            on("get contactsInfo") {
                val testObs = TestObserver<GetContactsInfo.Response>()
                getContactsInfo.execute().subscribe(testObs)
                it("should be isSelected is none") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.contactDto.none { it.isSelected } }
                    testObs.assertValue { it.contactDto.size == 3 }
                }
            }
        }
        given("base contacts empty, ignore contacts empty") {
            val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
            Mockito.`when`(contactsDataSource.getContacts()).then {
                Observable.just<List<Contact>>(listOf())
            }
            Mockito.`when`(contactsDataSource.getIgnoreContacts()).then {
                Observable.just<List<Contact>>(listOf())
            }
            val getContactsInfo = GetContactsInfo(contactsDataSource, executionThreads)

            on("get contactsInfo") {
                val testObs = TestObserver<GetContactsInfo.Response>()
                getContactsInfo.execute().subscribe(testObs)
                it("should empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.contactDto.isEmpty() }
                }
            }
        }
        given("base contacts empty, ignore contacts exist") {
            val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
            Mockito.`when`(contactsDataSource.getContacts()).then {
                Observable.just<List<Contact>>(listOf())
            }
            Mockito.`when`(contactsDataSource.getIgnoreContacts()).then {
                Observable.create<List<Contact>> {
                    val c1 = Contact().apply {
                        displayName = "あいうえお"
                        contactId = "12345"
                        phoneNumberString = "1234567890"
                    }
                    val c2 = Contact().apply {
                        displayName = "かきくけこ"
                        contactId = "22222"
                        phoneNumberString = "2222222222"
                    }
                    val list = listOf(c1, c2)
                    it.onNext(list)
                    it.onComplete()
                }
            }
            val getContactsInfo = GetContactsInfo(contactsDataSource, executionThreads)

            on("get contactsInfo") {
                val testObs = TestObserver<GetContactsInfo.Response>()
                getContactsInfo.execute().subscribe(testObs)
                it("should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.contactDto.isEmpty() }
                }
            }
        }

        given("base contacts exist, ignore contacts exist, base contacts contain ignore contacts") {
            val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
            val c1 = Contact().apply {
                displayName = "あいうえお"
                contactId = "12345"
                phoneNumberString = "1234567890"
            }
            val c2 = Contact().apply {
                displayName = "かきくけこ"
                contactId = "22222"
                phoneNumberString = "2222222222"
            }
            val c3 = Contact().apply {
                displayName = "さしすせそ"
                contactId = "33333"
                phoneNumberString = "3333333333"
            }
            Mockito.`when`(contactsDataSource.getContacts()).then {
                Observable.create<List<Contact>> {

                    val list = listOf(c1, c2, c3)
                    it.onNext(list)
                    it.onComplete()
                }
            }

            val i1 = Contact().apply {
                displayName = "あいうえお"
                contactId = "12345"
                phoneNumberString = "1234567890"
            }
            val i2 = Contact().apply {
                displayName = "かきくけこ"
                contactId = "22222"
                phoneNumberString = "2222222222"
            }
            Mockito.`when`(contactsDataSource.getIgnoreContacts()).then {
                Observable.create<List<Contact>> {
                    val list = listOf(i1, i2)
                    it.onNext(list)
                    it.onComplete()
                }
            }
            val getContactsInfo = GetContactsInfo(contactsDataSource, executionThreads)

            on("get contactsInfo") {
                val testObs = TestObserver<GetContactsInfo.Response>()
                getContactsInfo.execute().subscribe(testObs)
                it("should be exist, c1 and c2 is selected, c3 is not selected") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.contactDto.find { it.contact.contactId == c1.contactId }!!.isSelected }
                    testObs.assertValue { it.contactDto.find { it.contact.contactId == c2.contactId }!!.isSelected }
                    testObs.assertValue { !it.contactDto.find { it.contact.contactId == c3.contactId }!!.isSelected }
                }
            }
        }
        given("base contacts exist, ignore contacts exist, base contacts don't contain ignore contacts") {
            val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
            Mockito.`when`(contactsDataSource.getContacts()).then {
                Observable.create<List<Contact>> {
                    val c1 = Contact().apply {
                        displayName = "あいうえお"
                        contactId = "12345"
                        phoneNumberString = "1234567890"
                    }
                    val c2 = Contact().apply {
                        displayName = "かきくけこ"
                        contactId = "22222"
                        phoneNumberString = "2222222222"
                    }
                    val c3 = Contact().apply {
                        displayName = "さしすせそ"
                        contactId = "33333"
                        phoneNumberString = "3333333333"
                    }
                    val list = listOf(c1, c2, c3)
                    it.onNext(list)
                    it.onComplete()
                }
            }
            Mockito.`when`(contactsDataSource.getIgnoreContacts()).then {
                Observable.create<List<Contact>> {
                    val i1 = Contact().apply {
                        displayName = "あいうえお"
                        contactId = "44444"
                        phoneNumberString = "444444444"
                    }
                    val i2 = Contact().apply {
                        displayName = "かきくけこ"
                        contactId = "55555"
                        phoneNumberString = "555555555"
                    }
                    val list = listOf(i1, i2)
                    it.onNext(list)
                    it.onComplete()
                }
            }
            val getContactsInfo = GetContactsInfo(contactsDataSource, executionThreads)

            on("get contactsInfo") {
                val testObs = TestObserver<GetContactsInfo.Response>()
                getContactsInfo.execute().subscribe(testObs)
                it("should be exist, isSelected is none") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.contactDto.none { it.isSelected } }
                }
            }
        }

    }
})