package tech.ketc.ktil.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.*

actual val testDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

actual fun runTest(block: suspend CoroutineScope.() -> Unit): Unit = runBlocking { block() }
