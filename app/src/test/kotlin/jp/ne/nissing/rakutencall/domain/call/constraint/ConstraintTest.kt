package jp.ne.nissing.rakutencall.domain.call.constraint

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import jp.ne.nissing.rakutencall.data.datasource.contacts.ContactsDataSource
import jp.ne.nissing.rakutencall.data.datasource.settings.SettingDataSource
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import jp.ne.nissing.rakutencall.data.entity.prefix.*
import jp.ne.nissing.rakutencall.domain.call.ValidatePhoneNumber
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
object ConstraintTest : Spek({
    val executionThreads: ExecutionThreads = object : ExecutionThreads {
        override fun io(): Scheduler = Schedulers.trampoline()
        override fun ui(): Scheduler = Schedulers.trampoline()
    }
    val prefix: Prefix = Mockito.mock(Prefix::class.java)
    val emptyPrefix = EmptyPrefix()
    context("freeDial test") {
        val contactsDataSource: ContactsDataSource = Mockito.mock(ContactsDataSource::class.java)
        Mockito.`when`(contactsDataSource.getFreeDialPrefix()).then {
            Observable.create<List<FreeDialPrefix>> {
                val f1 = FreeDialPrefix("0120")
                val f2 = FreeDialPrefix("0800")
                val list = listOf(f1, f2)
                it.onNext(list)
                it.onComplete()
            }
        }


        given("enable true, 000 start") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getFreeDialEnable()).then { Observable.just(true) }
            val freeDial = FreeDial(settingDataSource, contactsDataSource, executionThreads)
            on("validate 00011112222") {
                val testObs = TestObserver<FreeDial.Response>()
                freeDial.execute(FreeDial.Request(prefix, PhoneNumber("00011112222"))).subscribe(testObs)
                it("prefix should not be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("enable false, 000 start") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getFreeDialEnable()).then { Observable.just(false) }
            val freeDial = FreeDial(settingDataSource, contactsDataSource, executionThreads)
            on("validate 00011112222") {
                val testObs = TestObserver<FreeDial.Response>()
                freeDial.execute(FreeDial.Request(prefix, PhoneNumber("00011112222"))).subscribe(testObs)
                it("prefix should not be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("enable false, 0120 start") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getFreeDialEnable()).then { Observable.just(false) }
            val freeDial = FreeDial(settingDataSource, contactsDataSource, executionThreads)
            on("validate 01201112222") {
                val testObs = TestObserver<FreeDial.Response>()
                freeDial.execute(FreeDial.Request(prefix, PhoneNumber("01201112222"))).subscribe(testObs)
                it("prefix should not be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("enable true, 0120 start") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getFreeDialEnable()).then { Observable.just(true) }
            val freeDial = FreeDial(settingDataSource, contactsDataSource, executionThreads)
            on("validate 01201112222") {
                val testObs = TestObserver<FreeDial.Response>()
                freeDial.execute(FreeDial.Request(prefix, PhoneNumber("01201112222"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
        given("enable true, 0800 start") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getFreeDialEnable()).then { Observable.just(true) }
            val freeDial = FreeDial(settingDataSource, contactsDataSource, executionThreads)
            on("validate 08001112222") {
                val testObs = TestObserver<FreeDial.Response>()
                freeDial.execute(FreeDial.Request(prefix, PhoneNumber("08001112222"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
        given("enable true, 0800 contains") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getFreeDialEnable()).then { Observable.just(true) }
            val freeDial = FreeDial(settingDataSource, contactsDataSource, executionThreads)
            on("validate 0000800111") {
                val testObs = TestObserver<FreeDial.Response>()
                freeDial.execute(FreeDial.Request(prefix, PhoneNumber("0000800111"))).subscribe(testObs)
                it("prefix should not be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
    }
    context("Ignore Number Test") {
        val contactsDataSource: ContactsDataSource = Mockito.mock(ContactsDataSource::class.java)
        Mockito.`when`(contactsDataSource.getIgnoreContacts()).then {
            Observable.create<List<Contact>> {
                val c1 = Contact().apply {
                    phoneNumberString = "09012345678"
                }
                val c2 = Contact().apply {
                    phoneNumberString = "09011112222"
                }
                val list = listOf(c1, c2)
                it.onNext(list)
                it.onComplete()
            }
        }

        given("same ignoreNumber") {
            val ignoreNumber = IgnoreNumber(contactsDataSource, executionThreads)
            on("validate 09011112222") {
                val testObs = TestObserver<IgnoreNumber.Response>()
                ignoreNumber.execute(IgnoreNumber.Request(prefix, PhoneNumber("09011112222"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }

        given("different ignoreNumber") {
            val ignoreNumber = IgnoreNumber(contactsDataSource, executionThreads)
            on("validate 00011112222") {
                val testObs = TestObserver<IgnoreNumber.Response>()
                ignoreNumber.execute(IgnoreNumber.Request(prefix, PhoneNumber("00011112222"))).subscribe(testObs)
                it("prefix should not be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }

        given("contains ignoreNumber") {
            val ignoreNumber = IgnoreNumber(contactsDataSource, executionThreads)
            on("validate 090123456789") {
                val testObs = TestObserver<IgnoreNumber.Response>()
                ignoreNumber.execute(IgnoreNumber.Request(prefix, PhoneNumber("090123456789"))).subscribe(testObs)
                it("prefix should not be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
    }
    context("InternationalNumber Test") {
        val contactsDataSource: ContactsDataSource = Mockito.mock(ContactsDataSource::class.java)
        Mockito.`when`(contactsDataSource.getInternationalIdentificationPrefix()).then {
            Observable.create<List<IdentificationPrefix>> {
                val i1 = IdentificationPrefix("010")
                val i2 = IdentificationPrefix("001010")
                val i3 = IdentificationPrefix("+")
                val list = listOf(i1, i2, i3)
                it.onNext(list)
                it.onComplete()
            }
        }
        Mockito.`when`(contactsDataSource.getEnableInternationalPrefix()).then {
            Observable.create<List<InternationalPrefix>> {
                val i1 = InternationalPrefix("82")
                val i2 = InternationalPrefix("55")
                val i3 = InternationalPrefix("7")
                val list = listOf(i1, i2, i3)
                it.onNext(list)
                it.onComplete()
            }
        }
        Mockito.`when`(contactsDataSource.getDisableInternationalPrefix()).then {
            Observable.create<List<InternationalPrefix>> {
                val i1 = InternationalPrefix("6189164")
                val i2 = InternationalPrefix("7909")
                val i3 = InternationalPrefix("7968")
                val list = listOf(i1, i2, i3)
                it.onNext(list)
                it.onComplete()
            }
        }

        given("setting false") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getInternationalNumEnable()).then { Observable.just(false) }
            val internationalNumber = InternationalNumber(settingDataSource, contactsDataSource, executionThreads)
            on("validate 010820000000") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("010820000000"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("setting true, start Id Prefix false") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getInternationalNumEnable()).then { Observable.just(true) }
            val internationalNumber = InternationalNumber(settingDataSource, contactsDataSource, executionThreads)
            on("validate 11112222255") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("11112222255"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("setting true, start Id Prefix true, start enablePrefix true, start disablePrefix false") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getInternationalNumEnable()).then { Observable.just(true) }
            val internationalNumber = InternationalNumber(settingDataSource, contactsDataSource, executionThreads)

            on("validate 010820000000") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("010820000000"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
            on("validate 010551111111") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("010551111111"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
            on("validate +71120231211") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("+71120231211"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
        given("setting true, start Id Prefix true, start enablePrefix true, start disablePrefix true") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getInternationalNumEnable()).then { Observable.just(true) }
            val internationalNumber = InternationalNumber(settingDataSource, contactsDataSource, executionThreads)

            on("validate 0107968111") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("0107968111"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
            on("validate 0010107909111") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("0010107909111"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
            on("validate +79681234111") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("+79681234111"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("setting true, start Id Prefix true, start enablePrefix false, start disablePrefix true") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getInternationalNumEnable()).then { Observable.just(true) }
            val internationalNumber = InternationalNumber(settingDataSource, contactsDataSource, executionThreads)

            on("validate 0106189164") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("0106189164"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }

        }
        given("setting true, start Id Prefix true, start enablePrefix false, start disablePrefix false") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getInternationalNumEnable()).then { Observable.just(true) }
            val internationalNumber = InternationalNumber(settingDataSource, contactsDataSource, executionThreads)

            on("validate 001010999999") {
                val testObs = TestObserver<InternationalNumber.Response>()
                internationalNumber.execute(InternationalNumber.Request(prefix, PhoneNumber("001010999999"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }


        }
    }
    context("PrefixEnable Test") {
        given("setting true") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getPrefixEnable()).then { Observable.just(true) }
            val prefixEnable = PrefixEnable(settingDataSource, executionThreads)
            on("validate") {
                val testObs = TestObserver<PrefixEnable.Response>()
                prefixEnable.execute(PrefixEnable.Request(prefix)).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("setting false") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getPrefixEnable()).then { Observable.just(false) }
            val prefixEnable = PrefixEnable(settingDataSource, executionThreads)
            on("validate") {
                val testObs = TestObserver<PrefixEnable.Response>()
                prefixEnable.execute(PrefixEnable.Request(prefix)).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
    }
    context("SpecialLength Test(Length=5)") {
        val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
        Mockito.`when`(contactsDataSource.getSpecialNumberLength()).then { Observable.just(5) }
        given("settings false, length 3") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getSpecialNumIgnoreEnable()).then { Observable.just(false) }
            val specialLength = SpecialLength(settingDataSource, contactsDataSource, executionThreads)
            on("validate") {
                val testObs = TestObserver<SpecialLength.Response>()
                specialLength.execute(SpecialLength.Request(prefix, PhoneNumber("123"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("settings true, length 3~6") {
            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getSpecialNumIgnoreEnable()).then { Observable.just(true) }
            val specialLength = SpecialLength(settingDataSource, contactsDataSource, executionThreads)
            on("validate 123") {
                val testObs = TestObserver<SpecialLength.Response>()
                specialLength.execute(SpecialLength.Request(prefix, PhoneNumber("123"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
            on("validate 1234") {
                val testObs = TestObserver<SpecialLength.Response>()
                specialLength.execute(SpecialLength.Request(prefix, PhoneNumber("1234"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
            on("validate 12345") {
                val testObs = TestObserver<SpecialLength.Response>()
                specialLength.execute(SpecialLength.Request(prefix, PhoneNumber("12345"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
            on("validate 123456") {
                val testObs = TestObserver<SpecialLength.Response>()
                specialLength.execute(SpecialLength.Request(prefix, PhoneNumber("123456"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
    }
    context("SpecialNumber Test") {
        val contactsDataSource = Mockito.mock(ContactsDataSource::class.java)
        Mockito.`when`(contactsDataSource.getSpecialNumberPrefix()).then {
            Observable.create<List<SpecialPrefix>> {
                val s1 = SpecialPrefix("#")
                val s2 = SpecialPrefix("*")
                val list = listOf(s1, s2)
                it.onNext(list)
                it.onComplete()
            }
        }
        given("setting false") {
            val settingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getSpecialNumIgnoreEnable()).then { Observable.just(false) }
            val specialNumber = SpecialNumber(settingDataSource, contactsDataSource, executionThreads)
            on("validate") {
                val testObs = TestObserver<SpecialNumber.Response>()
                specialNumber.execute(SpecialNumber.Request(prefix, PhoneNumber("12345678"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("setting true start special true") {
            val settingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getSpecialNumIgnoreEnable()).then { Observable.just(true) }
            val specialNumber = SpecialNumber(settingDataSource, contactsDataSource, executionThreads)
            on("validate #12345678") {
                val testObs = TestObserver<SpecialNumber.Response>()
                specialNumber.execute(SpecialNumber.Request(prefix, PhoneNumber("#12345678"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
            on("validate *12345678") {
                val testObs = TestObserver<SpecialNumber.Response>()
                specialNumber.execute(SpecialNumber.Request(prefix, PhoneNumber("*12345678"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
        given("setting true start special false") {
            val settingDataSource = Mockito.mock(SettingDataSource::class.java)
            Mockito.`when`(settingDataSource.getSpecialNumIgnoreEnable()).then { Observable.just(true) }
            val specialNumber = SpecialNumber(settingDataSource, contactsDataSource, executionThreads)
            on("validate 12345678") {
                val testObs = TestObserver<SpecialNumber.Response>()
                specialNumber.execute(SpecialNumber.Request(prefix, PhoneNumber("12345678"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
    }
    context("StartWithPrefix Test") {
        val settingDataSource = Mockito.mock(SettingDataSource::class.java)
        Mockito.`when`(settingDataSource.getPrefixNumber()).then { Observable.just(IgnorePrefix("003768")) }
        given("start with prefix true") {
            val startWithPrefix = StartWithPrefix(settingDataSource, executionThreads)
            on("validate 00376811112222") {
                val testObs = TestObserver<StartWithPrefix.Response>()
                startWithPrefix.execute(StartWithPrefix.Request(prefix, PhoneNumber("00376811112222"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
        given("start with prefix false") {
            val startWithPrefix = StartWithPrefix(settingDataSource, executionThreads)
            on("validate 00311112222") {
                val testObs = TestObserver<StartWithPrefix.Response>()
                startWithPrefix.execute(StartWithPrefix.Request(prefix, PhoneNumber("00311112222"))).subscribe(testObs)
                it("prefix should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == prefix }
                }
            }
        }
        given("prefix is already empty") {
            val startWithPrefix = StartWithPrefix(settingDataSource, executionThreads)
            on("validate 00311112222") {
                val testObs = TestObserver<StartWithPrefix.Response>()
                startWithPrefix.execute(StartWithPrefix.Request(emptyPrefix, PhoneNumber("00311112222"))).subscribe(testObs)
                it("prefix should be empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.prefix == EmptyPrefix() }
                }
            }
        }
    }
    context("validatePhoneNumber Test") {
        given("all validate pass") {
            val phoneNumber = PhoneNumber("12345678")
            val ignorePrefix = IgnorePrefix("003768")

            val settingDataSource: SettingDataSource = Mockito.mock(SettingDataSource::class.java)
            val prefixEnable: PrefixEnable = Mockito.mock(PrefixEnable::class.java)
            val freeDial: FreeDial = Mockito.mock(FreeDial::class.java)
            val ignoreNumber: IgnoreNumber = Mockito.mock(IgnoreNumber::class.java)
            val internationalNumber: InternationalNumber = Mockito.mock(InternationalNumber::class.java)
            val specialNumber: SpecialNumber = Mockito.mock(SpecialNumber::class.java)
            val specialLength: SpecialLength = Mockito.mock(SpecialLength::class.java)
            val startWithPrefix: StartWithPrefix = Mockito.mock(StartWithPrefix::class.java)

            Mockito.`when`(settingDataSource.getPrefixNumber()).then { Observable.just(ignorePrefix) }
            Mockito.`when`(prefixEnable.execute(PrefixEnable.Request(ignorePrefix))).then { Observable.just(PrefixEnable.Response(ignorePrefix)) }
            Mockito.`when`(freeDial.execute(FreeDial.Request(ignorePrefix, phoneNumber))).then { Observable.just(FreeDial.Response(ignorePrefix)) }
            Mockito.`when`(ignoreNumber.execute(IgnoreNumber.Request(ignorePrefix, phoneNumber))).then { Observable.just(IgnoreNumber.Response(ignorePrefix)) }
            Mockito.`when`(internationalNumber.execute(InternationalNumber.Request(ignorePrefix, phoneNumber))).then { Observable.just(InternationalNumber.Response(ignorePrefix)) }
            Mockito.`when`(specialNumber.execute(SpecialNumber.Request(ignorePrefix, phoneNumber))).then { Observable.just(SpecialNumber.Response(ignorePrefix)) }
            Mockito.`when`(specialLength.execute(SpecialLength.Request(ignorePrefix, phoneNumber))).then { Observable.just(SpecialLength.Response(ignorePrefix)) }
            Mockito.`when`(startWithPrefix.execute(StartWithPrefix.Request(ignorePrefix, phoneNumber))).then { Observable.just(StartWithPrefix.Response(ignorePrefix)) }

            val validatePhoneNumber = ValidatePhoneNumber(settingDataSource, prefixEnable, freeDial, ignoreNumber, internationalNumber, specialNumber, specialLength, startWithPrefix, executionThreads)

            on("validate 12345678") {
                val testObs = TestObserver<ValidatePhoneNumber.Response>()
                validatePhoneNumber.execute(ValidatePhoneNumber.Request(phoneNumber)).subscribe(testObs)
                it("should be not empty") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.phoneNumber.number == "00376812345678" }
                }
            }
        }
        given("some validate return empty") {
            val phoneNumber = PhoneNumber("11111111")
            val ignorePrefix = IgnorePrefix("003768")
            val responsePrefix: Prefix = IgnorePrefix("003768")
            val settingDataSource: SettingDataSource = Mockito.spy(SettingDataSource::class.java)
            val prefixEnable: PrefixEnable = Mockito.mock(PrefixEnable::class.java)
            val freeDial: FreeDial = Mockito.mock(FreeDial::class.java)
            val ignoreNumber: IgnoreNumber = Mockito.mock(IgnoreNumber::class.java)
            val internationalNumber: InternationalNumber = Mockito.mock(InternationalNumber::class.java)
            val specialNumber: SpecialNumber = Mockito.mock(SpecialNumber::class.java)
            val specialLength: SpecialLength = Mockito.mock(SpecialLength::class.java)
            val startWithPrefix: StartWithPrefix = Mockito.mock(StartWithPrefix::class.java)

            Mockito.`when`(settingDataSource.getPrefixNumber()).then { Observable.just(responsePrefix) }
            Mockito.`when`(prefixEnable.execute(PrefixEnable.Request(ignorePrefix))).then { Observable.just(PrefixEnable.Response(responsePrefix)) }
            Mockito.`when`(freeDial.execute(FreeDial.Request(ignorePrefix, phoneNumber))).then { Observable.just(FreeDial.Response(responsePrefix)) }
            Mockito.`when`(ignoreNumber.execute(IgnoreNumber.Request(ignorePrefix, phoneNumber))).then { Observable.just(IgnoreNumber.Response(responsePrefix)) }
            Mockito.`when`(internationalNumber.execute(InternationalNumber.Request(ignorePrefix, phoneNumber))).then { Observable.just(InternationalNumber.Response(responsePrefix)) }
            Mockito.`when`(specialNumber.execute(SpecialNumber.Request(ignorePrefix, phoneNumber))).then { Observable.just(SpecialNumber.Response(responsePrefix)) }
            Mockito.`when`(specialLength.execute(SpecialLength.Request(ignorePrefix, phoneNumber))).then { Observable.just(SpecialLength.Response(EmptyPrefix())) }
            Mockito.`when`(startWithPrefix.execute(StartWithPrefix.Request(EmptyPrefix(), phoneNumber))).then { Observable.just(StartWithPrefix.Response(EmptyPrefix())) }

            val validatePhoneNumber = ValidatePhoneNumber(settingDataSource, prefixEnable, freeDial, ignoreNumber, internationalNumber, specialNumber, specialLength, startWithPrefix, executionThreads)


            on("validate 11111111") {
                val testObs = TestObserver<ValidatePhoneNumber.Response>()
                validatePhoneNumber.execute(ValidatePhoneNumber.Request(phoneNumber)).subscribe(testObs)
                it("should be 11111111") {
                    testObs.assertNoErrors()
                    testObs.assertValue { it.phoneNumber.number == "11111111" }
                }
            }
        }
    }
})