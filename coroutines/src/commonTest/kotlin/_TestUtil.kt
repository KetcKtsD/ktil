package tech.ketc.ktil.coroutines

import kotlinx.coroutines.*

expect val testDispatcher: CoroutineDispatcher
expect fun runTest(block: suspend CoroutineScope.() -> Unit)
