package com.sechkarev.hiraganateacherkmp.utils

sealed interface LengthyTask<out T : Any?> {
    data object InProgress : LengthyTask<Nothing>

    data class Success<T : Any?>(
        val result: T,
    ) : LengthyTask<T>

    data class Error(
        val cause: Throwable,
    ) : LengthyTask<Nothing>
}
