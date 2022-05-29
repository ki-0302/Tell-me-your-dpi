package com.maho_ya.tell_me_your_dpi.domain

import com.maho_ya.tell_me_your_dpi.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class UseCase <out R> {

    suspend operator fun invoke(): Result<R> {

        return try {
            withContext(Dispatchers.IO) {
                execute().let {
                    Result.Success(it)
                }
            }
        } catch (e: Exception) {
            Timber.d(e)
            Result.Error(Exception(e))
        }
    }

    protected abstract suspend fun execute(): R
}
