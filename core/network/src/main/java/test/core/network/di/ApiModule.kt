package test.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import test.core.network.api.LessonsApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLessonsApiService(retrofit: Retrofit): LessonsApiService {
        return retrofit.create(LessonsApiService::class.java)
    }
}