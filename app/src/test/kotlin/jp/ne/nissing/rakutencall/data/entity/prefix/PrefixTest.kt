package jp.ne.nissing.rakutencall.data.entity.prefix

import jp.ne.nissing.rakutencall.data.entity.number.PhoneNumber
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object PrefixTest : Spek({
    given("EmptyPrefix Test") {
        val emptyPrefix: Prefix = EmptyPrefix()

        on("check number") {
            it("should be empty") {
                assert(emptyPrefix.number.isEmpty())
            }
        }
        on("equal method") {
            it("should be equal EmptyPrefix") {
                val expectedPrefix = EmptyPrefix()
                assert(emptyPrefix == expectedPrefix)
            }
            it("should be not equal") {
                val expectedPrefix = IgnorePrefix("12345")
                assert(emptyPrefix != expectedPrefix)
            }
            it("should be equal IgnorePrefix(empty)") {
                val expectedPrefix = IgnorePrefix("")
                assert(emptyPrefix == expectedPrefix)
            }
        }
    }
    given("FreeDialPrefix Test") {
        val freeDialPrefix: Prefix = FreeDialPrefix("12345")
        on("equal method") {
            it("should not be equal EmptyPrefix") {
                val expectedPrefix = EmptyPrefix()
                assert(freeDialPrefix != expectedPrefix)
            }
        }
    }
    given("IdentificationPrefix Test") {
        val identificationPrefix: Prefix = IdentificationPrefix("12345")
        on("equal method") {
            it("should not be equal EmptyPrefix") {
                val expectedPrefix = EmptyPrefix()
                assert(identificationPrefix != expectedPrefix)
            }
        }
    }
    given("IgnorePrefix Test") {
        val ignorePrefix: Prefix = IgnorePrefix("12345")
        on("equal method") {
            it("should not be equal EmptyPrefix") {
                val expectedPrefix = EmptyPrefix()
                assert(ignorePrefix != expectedPrefix)
            }
        }
    }
    given("InternationalPrefix Test") {
        val internationalPrefix: Prefix = InternationalPrefix("12345")
        on("equal method") {
            it("should not be equal EmptyPrefix") {
                val expectedPrefix = EmptyPrefix()
                assert(internationalPrefix != expectedPrefix)
            }
        }
    }
    given("SpecialPrefix Test") {
        val specialPrefix: Prefix = SpecialPrefix("12345")
        on("equal method") {
            it("should not be equal EmptyPrefix") {
                val expectedPrefix = EmptyPrefix()
                assert(specialPrefix != expectedPrefix)
            }
        }
    }
    given("Prefix Test") {
        val ignorePrefix: Prefix = IgnorePrefix("12345")
        on("startWith method") {
            it("should be true ") {
                assert(ignorePrefix.startWithPrefix(PhoneNumber("123456789")))
            }
            it("should be false") {
                assert(!ignorePrefix.startWithPrefix(PhoneNumber("0123456789")))
            }
        }
    }
})