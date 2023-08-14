package test.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LessonsNetworkRequest(val lessons: List<NetworkLesson>)
