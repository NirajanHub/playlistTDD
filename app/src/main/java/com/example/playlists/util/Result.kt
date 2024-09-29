package com.example.playlists.util

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Error<T>(val message: String?) : Result<T>()
    data object Loading : Result<Nothing>()

}
enum class Output{
    SUCCESS,
    FAILURE
}

