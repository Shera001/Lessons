package test.core.domain.use_case

import test.core.domain.repository.LessonsRepository
import javax.inject.Inject

class SaveLessonUseCase @Inject constructor(
    private val repository: LessonsRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.saveLesson(id)
    }
}