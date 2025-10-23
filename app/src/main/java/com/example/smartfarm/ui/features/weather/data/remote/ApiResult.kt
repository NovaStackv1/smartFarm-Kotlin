package com.example.smartfarm.ui.features.weather.data.remote

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception, val message: String? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}