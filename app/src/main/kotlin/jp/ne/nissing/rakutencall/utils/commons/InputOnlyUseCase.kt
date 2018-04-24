package jp.ne.nissing.rakutencall.utils.commons

abstract class InputOnlyUseCase<in Q : UseCase.RequestValue>(executionThreads: ExecutionThreads)
    : IoUseCase<Q, UseCase.NoResponseValue>(executionThreads)
