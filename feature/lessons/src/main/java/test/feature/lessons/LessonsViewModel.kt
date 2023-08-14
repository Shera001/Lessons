package test.feature.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import test.core.domain.use_case.GetAllLessonsUseCase
import test.core.model.Lesson
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val getAllLessonsUseCase: GetAllLessonsUseCase,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _lessons = MutableSharedFlow<List<Lesson>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val lessons: SharedFlow<List<Lesson>> = _lessons.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllLessonsUseCase().collect {
                _isLoading.emit(true)
                _lessons.emit(it)
                _isLoading.emit(false)
            }
        }
    }
}