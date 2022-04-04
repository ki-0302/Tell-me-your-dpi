package com.maho_ya.tell_me_your_dpi.domain

import com.maho_ya.tell_me_your_dpi.result.Result
import java.lang.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase <out R> {

    suspend operator fun invoke(): Flow<Result<R>> {

        return execute()
            .catch { e ->
                emit(Result.Error(Exception(e)))
            }
            .flowOn(Dispatchers.IO)
    }

    protected abstract suspend fun execute(): Flow<com.maho_ya.tell_me_your_dpi.result.Result<R>>
}
