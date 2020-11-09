package tech.ketc.ktil.coroutines

import kotlinx.coroutines.*

/**
 * Represent A Background joinable task.
 *
 * For example,
 * if want to do a job join but don't want to call a cancel, can wrap it in this interface
 */
interface Joinable {

    /**
     * Suspends the coroutine until this task is complete.
     */
    suspend fun join()
}

private class JoinableJob(private val job: Job) : Joinable {
    override suspend fun join() = job.join()
}

fun Job.toJoinable(): Joinable = JoinableJob(this)
