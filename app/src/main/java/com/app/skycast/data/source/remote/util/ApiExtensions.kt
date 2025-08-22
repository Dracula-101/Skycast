package com.app.skycast.data.source.remote.util
import com.app.skycast.data.source.remote.adapter.ResultCall
import retrofit2.Call

/**
 * Synchronously executes the [Call] and returns the [Result].
 */
inline fun <reified T : Any> Call<T>.executeForResult(): Result<T> =
    this
        .toResultCall()
        .executeForResult()

/**
 * Wraps the existing [Call] in a [ResultCall].
 */
inline fun <reified T : Any> Call<T>.toResultCall(): ResultCall<T> =
    ResultCall(
        backingCall = this,
        successType = T::class.java,
    )


/**
 * Flat maps a successful [Result] with the given [transform] to another [Result], and leaves
 * failures untouched.
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> =
    this.exceptionOrNull()
        ?.asFailure()
        ?: transform(this.getOrThrow())

/**
 * Returns the given receiver of type [T] as a "success" [Result].
 */
fun <T> T.asSuccess(): Result<T> =
    Result.success(this)

/**
 * Returns the given [Throwable] as a "failure" [Result].
 */
fun Throwable.asFailure(): Result<Nothing> =
    Result.failure(this)