package jp.ne.nissing.rakutencall.utils.commons

import io.reactivex.Observable

abstract class OutputOnlyUseCase<R : UseCase.ResponseValue>(executionThreads: ExecutionThreads)
    : IoUseCase<UseCase.NoRequestValue, R>(executionThreads) {
    protected abstract fun execute(): Observable<R>
    override fun execute(requestValue: NoRequestValue): Observable<R> = execute()
    fun execute(next: (R) -> Unit = {}, error: (Throwable) -> Unit = {}, complete: () -> Unit = {}) = execute(NoRequestValue.INSTANCE, next, error, complete)
}
