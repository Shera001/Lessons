package test.core.data.mapper

import test.core.model.Lesson
import test.core.network.model.NetworkLesson


internal fun NetworkLesson.toLesson(isOpen: Boolean) = Lesson(
    id = this.id,
    name = this.name,
    description = this.description,
    thumbnail = this.thumbnail,
    videoUrl = this.video_url,
    isOpen = isOpen
)

internal fun List<NetworkLesson>.toLessonList(savedIds: List<Int?>): List<Lesson> {
    val defaultFreeLessons = this.take(3).map { it.toLesson(true) }
    return defaultFreeLessons.plus(
        this.drop(3).map { it.toLesson(savedIds.contains(it.id)) }
    )
}