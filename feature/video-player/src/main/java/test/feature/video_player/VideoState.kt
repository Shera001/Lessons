package test.feature.video_player


data class VideoState(
    var currentItem: Int = 0,
    var playbackPosition: Long = 0,
    var id: Int? = null,
    var orientation: Orientation = Orientation.PORTRAIT
)

enum class Orientation {
    PORTRAIT,
    LANDSCAPE
}
