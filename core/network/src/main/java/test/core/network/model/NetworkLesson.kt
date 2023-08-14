package test.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLesson(
    val id: Int,
    val name: String,
    var description: String,
    val thumbnail: String,
    val video_url: String
)
