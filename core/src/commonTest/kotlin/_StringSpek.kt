package tech.ketc.ktil

import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*

class ExtStringSpek : Spek({
    describe("_String Spek") {
        it("String.strictLengthはサロゲートペアを正しくカウントする") {
            assertEquals(1, "😊".strictLength)
        }
    }
})
