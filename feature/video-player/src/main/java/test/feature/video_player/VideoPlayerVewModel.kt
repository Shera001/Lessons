package test.feature.video_player

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
class VideoPlayerVewModel @Inject constructor(
    private val getAllLessonsUseCase: GetAllLessonsUseCase,
) : ViewModel() {

    var videoState: VideoState = VideoState(0, 0)

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _currentLesson = MutableSharedFlow<Lesson>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val currentLesson: SharedFlow<Lesson> get() = _currentLesson.asSharedFlow()

    private val _lessons = MutableSharedFlow<List<Lesson>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val lessons: SharedFlow<List<Lesson>> get() = _lessons.asSharedFlow()

    var list: List<Lesson> = emptyList()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog.asStateFlow()

    init {
        viewModelScope.launch {
            getAllLessonsUseCase().collect { items ->
                list = items
                videoState.id?.let { getAllLessons(it) }
            }
        }
    }

    fun getAllLessons(id: Int) {
        viewModelScope.launch {
            _lessons.emit(list.filter { it.id != id })
            _isLoading.emit(false)
        }
    }

    fun getLessonById(id: Int) {
        val newLesson = list.find { it.id == id }
        newLesson?.let {
            viewModelScope.launch {
                _currentLesson.emit(it)
            }
        }
    }

    fun setNextLesson(nextLesson: Lesson) {
        viewModelScope.launch {
            with(videoState) {
                playbackPosition = 0
                currentItem = 0
                id = nextLesson.id
            }
            _currentLesson.emit(nextLesson)
            getAllLessons(nextLesson.id)
        }
    }

    fun setPreviousLesson(prevLesson: Lesson) {
        viewModelScope.launch {
            with(videoState) {
                playbackPosition = 0
                currentItem = 0
                id = prevLesson.id
            }
            _currentLesson.emit(prevLesson)
            getAllLessons(prevLesson.id)
        }
    }
}