package test.core.domain.use_case

import kotlinx.coroutines.flow.Flow
import test.core.domain.repository.LessonsRepository
import test.core.model.Lesson
import javax.inject.Inject

class GetAllLessonsUseCase @Inject constructor(
    private val repository: LessonsRepository
) {

    operator fun invoke(): Flow<List<Lesson>> =
        repository.getAllLessons()
}