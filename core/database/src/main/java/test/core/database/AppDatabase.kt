package test.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import test.core.database.dao.LessonsDao
import test.core.database.entity.LessonEntity

@Database(
    entities = [LessonEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun lessonsDao(): LessonsDao
}