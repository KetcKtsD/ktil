package tech.ketc.ktil.coroutines

import kotlinx.coroutines.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*

class JoinableSpek : Spek({
    describe("Joinable Spek") {
        describe("JoinableJob") {
            it("joinableはJobのjoinを呼び出す") {
                var lauched = false
                runTest { launch(testDispatcher) { delay(300); lauched = true }.toJoinable().join() }
                assertTrue(lauched)
            }
        }
    }
})
