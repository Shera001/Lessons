package test.core.model

data class Lesson(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val videoUrl: String,
    val isOpen: Boolean
)