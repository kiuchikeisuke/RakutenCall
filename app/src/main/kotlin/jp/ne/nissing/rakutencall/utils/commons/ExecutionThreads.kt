package jp.ne.nissing.rakutencall.utils.commons

import io.reactivex.Scheduler

interface ExecutionThreads {
    fun io(): Scheduler
    fun ui(): Scheduler
}
