package test.core.network.api

import retrofit2.http.GET
import test.core.network.model.LessonsNetworkRequest

interface LessonsApiService {

    @GET("lessons")
    suspend fun getLessons(): LessonsNetworkRequest
}