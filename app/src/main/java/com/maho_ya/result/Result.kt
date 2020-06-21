package com.maho_ya.result

sealed class Result<out R> {

    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

// Kotlin Custom Property. Return data on success.
val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data