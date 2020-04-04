package com.amamode.realestatemanager.utils

sealed class Resource<out T: Any> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val error: Throwable) : Resource<Nothing>()
    class Loading(val refresh: Boolean = false) : Resource<Nothing>()
}
