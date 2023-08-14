package test.core.domain.repository

import kotlinx.coroutines.flow.Flow
import test.core.model.Lesson

interface LessonsRepository {

    fun getAllLessons(): Flow<List<Lesson>>

    fun getAllSaveLessonsLessons(): Flow<List<Int?>>

    suspend fun saveLesson(id: Int)
}