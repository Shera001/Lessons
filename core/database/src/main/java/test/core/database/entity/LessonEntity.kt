package test.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LessonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val lessonId: Int?
)
