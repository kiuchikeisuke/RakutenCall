package jp.ne.nissing.rakutencall.data.entity.number

import jp.ne.nissing.rakutencall.data.entity.prefix.IgnorePrefix
import junit.framework.Assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
object NumberTest : Spek({
    describe("PhoneNumber Test") {
        context("effective number") {
            on("init method") {
                it("should pass init") {
                    Assert.assertNotNull(PhoneNumber("123456789"))
                }
                it("should pass init(# start)") {
                    Assert.assertNotNull(PhoneNumber("#123456789"))
                }
                it("should pass init (contains *)") {
                    Assert.assertNotNull(PhoneNumber("1234*56789"))
                }
                it("should pass init (contains +)") {
                    Assert.assertNotNull(PhoneNumber("123456789+"))
                }
                it("should pass init (contains -)") {
                    Assert.assertNotNull(PhoneNumber("123-456-789"))
                }
                it("should pass init (contains ())") {
                    Assert.assertNotNull(PhoneNumber("123(45)6789"))
                }
                it("should pass init (contains space)") {
                    Assert.assertNotNull(PhoneNumber("123 456 789"))
                }
            }
            given("ASCII Code Input") {
                on("decodeToUTF8PhoneNumber: %23 (#)") {
                    it("should be ") {
                        val expectedPhoneNumber = PhoneNumber("#12344445555")
                        assertEquals(expectedPhoneNumber, PhoneNumber.decodeToUTF8PhoneNumber("%2312344445555"))
                    }
                }
                on("decodeToUTF8PhoneNumber: %2A (*)") {
                    it("should be ") {
                        val expectedPhoneNumber = PhoneNumber("123*44445555")
                        assertEquals(expectedPhoneNumber, PhoneNumber.decodeToUTF8PhoneNumber("123%2A44445555"))
                    }
                }
                on("decodeToUTF8PhoneNumber: %2B (+)") {
                    it("should be ") {
                        val expectedPhoneNumber = PhoneNumber("12344445555+")
                        assertEquals(expectedPhoneNumber, PhoneNumber.decodeToUTF8PhoneNumber("12344445555%2B"))
                    }
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
            given("ASCII Code Input") {
                on("init: %23 (#)") {
                    it("should be IllegalArgumentException") {
                        assertFailsWith(IllegalArgumentException::class, { PhoneNumber("%2312344445555") })
                    }
                }
                on("init: %2A (*)") {
                    it("should be IllegalArgumentException") {
                        assertFailsWith(IllegalArgumentException::class, { PhoneNumber("123%2A44445555") })
                    }
                }
                on("init: %2B (+)") {
                    it("should be IllegalArgumentException") {
                        assertFailsWith(IllegalArgumentException::class, { PhoneNumber("12344445555%2B") })
                    }
                }
            }
            on("init: phone number empty") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { PhoneNumber("") })
                }
            }
            on("not allow string 123.456") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { PhoneNumber("123.456") })
                }
            }
            on("not allow string mogemoge") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { PhoneNumber("mogemoge") })
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
                it("should pass init(# start)") {
                    Assert.assertNotNull(TelephoneNumber("tel:#123456789"))
                }
                it("should pass init (contains *)") {
                    Assert.assertNotNull(TelephoneNumber("tel:1234*56789"))
                }
                it("should pass init (contains +)") {
                    Assert.assertNotNull(TelephoneNumber("tel:123456789+"))
                }
                it("should pass init (contains -)") {
                    Assert.assertNotNull(TelephoneNumber("tel:123-45-6789"))
                }
                it("should pass init (contains ())") {
                    Assert.assertNotNull(TelephoneNumber("tel:123(45)6789"))
                }
                it("should pass init (contains space)") {
                    Assert.assertNotNull(TelephoneNumber("tel:123 45 6789"))
                }
            }
            given("ASCII Code Input") {
                on("decodeToUTF8PhoneNumber: %23 (#)") {
                    it("should be ") {
                        val expected = TelephoneNumber("tel:#12344445555")
                        assertEquals(expected, TelephoneNumber.decodeToUTF8TelephoneNumber("tel:%2312344445555"))
                    }
                }
                on("decodeToUTF8PhoneNumber: %2A (*)") {
                    it("should be ") {
                        val expected = TelephoneNumber("tel:123*44445555")
                        assertEquals(expected, TelephoneNumber.decodeToUTF8TelephoneNumber("tel:123%2A44445555"))
                    }
                }
                on("decodeToUTF8PhoneNumber: %2B (+)") {
                    it("should be ") {
                        val expected = TelephoneNumber("tel:12344445555+")
                        assertEquals(expected, TelephoneNumber.decodeToUTF8TelephoneNumber("tel:12344445555%2B"))
                    }
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
            given("ASCII Code Input") {
                on("init: %23 (#)") {
                    it("should be IllegalArgumentException") {
                        assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("tel:%2312344445555") })
                    }
                }
                on("init: %2A (*)") {
                    it("should be IllegalArgumentException") {
                        assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("tel:123%2A44445555") })
                    }
                }
                on("init: %2B (+)") {
                    it("should be IllegalArgumentException") {
                        assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("tel:12344445555%2B") })
                    }
                }
            }
            on("init: telephoneNumber not start tel:") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("123456789") })
                }
            }
            on("not allow string 123.456") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("tel:123.456") })
                }
            }
            on("not allow string mogemoge") {
                it("should be IllegalArgumentException") {
                    assertFailsWith(IllegalArgumentException::class, { TelephoneNumber("tel:mogemoge") })
                }
            }

        }
    }
})