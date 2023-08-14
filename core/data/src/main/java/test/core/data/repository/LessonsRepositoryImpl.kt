package test.core.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import test.core.data.mapper.toLessonList
import test.core.database.dao.LessonsDao
import test.core.database.entity.LessonEntity
import test.core.domain.repository.LessonsRepository
import test.core.model.Lesson
import test.core.network.api.LessonsApiService
import javax.inject.Inject

class LessonsRepositoryImpl @Inject constructor(
    private val apiService: LessonsApiService,
    private val lessonsDao: LessonsDao
) : LessonsRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllLessons(): Flow<List<Lesson>> {
        return getAllSaveLessonsLessons().flatMapConcat {
            val lessonsList = try {
                apiService.getLessons().lessons
            } catch (t: Throwable) {
                Log.e("TAG", "getLessons: ", t)
                emptyList()
            }
            flow {
                emit(lessonsList.toLessonList(it))
            }
        }
    }

    override fun getAllSaveLessonsLessons(): Flow<List<Int?>> {
        return lessonsDao.getAllSavedLessons().map { items -> items.map { item -> item.lessonId } }
    }

    override suspend fun saveLesson(id: Int) = withContext(Dispatchers.IO) {
        lessonsDao.insert(LessonEntity(lessonId = id))
    }
}