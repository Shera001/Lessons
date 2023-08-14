package test.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import test.core.database.entity.LessonEntity

@Dao
interface LessonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lessonEntity: LessonEntity)

    @Query("SELECT * FROM LessonEntity")
    fun getAllSavedLessons(): Flow<List<LessonEntity>>
}