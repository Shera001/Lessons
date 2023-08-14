package test.feature.lessons.navigation


sealed interface LessonsDirections {
    data class ToVideoPlayerFragment(val id: Int) : LessonsDirections
}