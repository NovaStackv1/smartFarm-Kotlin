package com.example.smartfarm.ui.features.weather.data.remote

import retrofit2.Response


suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error(Exception("Response body is null"))
        } else {
            ApiResult.Error(
                Exception("API Error: ${response.code()}"),
                response.message()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e, e.message)
    }
}