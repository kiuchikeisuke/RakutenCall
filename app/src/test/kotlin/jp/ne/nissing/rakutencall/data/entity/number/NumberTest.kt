package jp.ne.nissing.rakutencall.data.entity.number

import jp.ne.nissing.rakutencall.data.entity.prefix.IgnorePrefix
import junit.framework.Assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
object NumberTest : Spek({
    describe("PhoneNumber Test") {
        context("effective number") {
            on("init method") {
                it("should pass init") {
                    Assert.assertNotNull(PhoneNumber("123456789"))
                }
            }
            on("equals method") {
                val actualPhoneNumber = PhoneNumber("123456789")
                it("should be equals") {
                    Assert.assertEquals(PhoneNumber("123456789"), actualPhoneNumber)
                }
                it("should not be equals") {
                    Assert.assertNotSame(PhoneNumber("123"), actualPhoneNumber)
                }
            }
            on("add method") {
                val actualPhoneNumber = PhoneNumber("123456789")
                val expectedPhoneNumber = PhoneNumber("5555123456789")
                val prefix = IgnorePrefix("5555")
                it("should be equals 5555123456789") {
                    Assert.assertEquals(expectedPhoneNumber, actualPhoneNumber.addPrefix(prefix))
                }
            }
            on("remove method") {
                val actualPhoneNumber = PhoneNumber("5555123456789")
                it("should be removed prefix") {
                    val prefix = IgnorePrefix("5555")
                    val expectedPhoneNumber = PhoneNumber("123456789")
                    Assert.assertEquals(expectedPhoneNumber, actualPhoneNumber.removePrefix(prefix))
                }
                it("should not be removed prefix") {
                    val prefix = IgnorePrefix("4444")
                    val expectedPhoneNumber = PhoneNumber("5555123456789")
                    Assert.assertEquals(expectedPhoneNumber, actualPhoneNumber.removePrefix(prefix))

                }
            }
            on("startWithPrefix method") {
                val actualPhoneNumber = PhoneNumber("5555123456789")
                it("should be true") {
                    val prefix = IgnorePrefix("5555")
                    Assert.assertTrue(actualPhoneNumber.startWithPrefix(prefix))
                }
                it("should be false") {
                    val prefix = IgnorePrefix("4444")
                    Assert.assertFalse(actualPhoneNumber.startWithPrefix(prefix))
                }
            }
            on("generateTelephoneNumber method") {
                val actualPhoneNumber = PhoneNumber("5555123456789")
                it("should be equals tel:5555123456789") {
                    val expectedTelephoneNumber = TelephoneNumber("tel:5555123456789")
                    Assert.assertEquals(expectedTelephoneNumber, actualPhoneNumber.generateTelephoneNumber())
                }
            }
        }
        context("illegal number") {
            on("init: phone number start tel:") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { PhoneNumber("tel:123456789") })
                }
                it("IllegalArgumentException: number must not be Empty") {
                    assertFailsWith(IllegalArgumentException::class, { PhoneNumber("") })
                }
            }

            on("init: phone number empty") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { PhoneNumber("") })
                }
            }
        }
    }

    describe("TelephoneNumber Test") {
        context("effective number") {
            on("init method") {
                it("should pass init") {
                    Assert.assertNotNull(TelephoneNumber("tel:123456789"))
                }
            }
            on("generatePhoneNumber test") {
                it("should be equals 123456789") {
                    val expectedPhoneNumber = PhoneNumber("123456789")
                    Assert.assertEquals(expectedPhoneNumber, TelephoneNumber("tel:123456789").generatePhoneNumber())
                }
            }
        }
        context("illegal number") {
            on("init: empty telephoneNumber") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("") })
                }
            }
            on("init: telephoneNumber not start tel:") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("123456789") })
                }
            }

        }
    }
})