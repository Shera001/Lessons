package test.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.core.database.AppDatabase
import test.core.database.dao.LessonsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            name = "test-app-database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideLessonsDao(database: AppDatabase): LessonsDao = database.lessonsDao()
}