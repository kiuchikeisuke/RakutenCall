package jp.ne.nissing.rakutencall.utils.commons

import dagger.internal.Preconditions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber


abstract class IoUseCase<in Q : UseCase.RequestValue, R : UseCase.ResponseValue, T : Throwable>(private val executionThreads: ExecutionThreads) : UseCase<Q, R>() {
    private val disposable: CompositeDisposable = CompositeDisposable()

    @Suppress("UNCHECKED_CAST")
    fun execute(requestValues: Q, next: (R) -> Unit = {}, error: (T) -> Unit = { Timber.d(it) }, complete: () -> Unit = {}): Observable<R> {
        disposable.clear()
        val observable = execute(requestValues)
                .subscribeOn(executionThreads.io())

        addDisposable(observable.subscribe(next, error as (Throwable) -> Unit, complete))
        return observable
    }

    fun dispose() {
        if (disposable.isDisposed) {
            disposable.dispose()
        }
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        Preconditions.checkNotNull(disposable)
        Preconditions.checkNotNull(this.disposable)
        this.disposable.add(disposable)
    }
}

