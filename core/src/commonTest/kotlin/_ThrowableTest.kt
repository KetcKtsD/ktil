package tech.ketc.ktil

import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*

class ExtThrowableSpek : Spek({
    describe("_Throwable Spek") {
        it("Throwable.raiseは自身をthrowする") {
            val e = Exception()
            assertEquals(e, assertFails { e.raise() })
        }
    }
})
