package test.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import test.core.domain.use_case.SaveLessonUseCase
import javax.inject.Inject


@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val saveLessonUseCase: SaveLessonUseCase
) : ViewModel() {

    fun saveLesson(id: Int) {
        viewModelScope.launch {
            saveLessonUseCase(id)
        }
    }
}