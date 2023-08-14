package test.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.core.data.repository.LessonsRepositoryImpl
import test.core.domain.repository.LessonsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindLessonRepository(lessonsRepository: LessonsRepositoryImpl): LessonsRepository
}