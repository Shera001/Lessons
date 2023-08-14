package test.feature.video_player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import test.core.model.Lesson
import test.core.ui.PurchaseFragmentDialog
import test.core.ui.adapter.LessonsAdapter
import test.core.ui.adapter.LessonsItemDecorator
import test.core.ui.adapter.PlaceholderAdapter
import test.feature.video_player.databinding.FragmentVideoPlayerBinding


@AndroidEntryPoint
class VideoPlayerFragment : Fragment() {

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding: FragmentVideoPlayerBinding get() = requireNotNull(_binding)

    private var videoPlayer: ExoPlayer? = null

    private var lessonsAdapter: LessonsAdapter? = null

    private val viewModel by viewModels<VideoPlayerVewModel>()

    private var currentOrientation = Orientation.PORTRAIT
    private var currentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentOrientation = viewModel.videoState.orientation

        if (viewModel.videoState.id == null) {
            currentId = arguments?.getInt("id")
            viewModel.videoState.id = currentId
        } else {
            currentId = viewModel.videoState.id
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPlayerBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDate()
        subscribeToFlow()
        setUpListeners()

        activity?.onBackPressedDispatcher?.addCallback(this, enabled = true) {
            when (currentOrientation) {
                Orientation.PORTRAIT -> {
                    findNavController().navigateUp()
                }

                Orientation.LANDSCAPE -> {
                    currentOrientation = Orientation.PORTRAIT
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()
    }

    private fun setDate() {
        val fullScreenIv = binding.root.findViewById<ImageView>(R.id.bt_fullscreen)

        if (currentOrientation == Orientation.PORTRAIT) {
            fullScreenIv.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_fullscreen
                )
            )
            bindAdapter()
        } else {
            fullScreenIv.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_fullscreen_exit
                )
            )
        }

        fullScreenIv.setOnClickListener {
            when (viewModel.videoState.orientation) {
                Orientation.PORTRAIT -> {
                    currentOrientation = Orientation.LANDSCAPE
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

                Orientation.LANDSCAPE -> {
                    currentOrientation = Orientation.PORTRAIT
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
    }

    private fun setUpListeners() {
        val prevIv = binding.player.findViewById<ImageView>(R.id.prevIv)
        val nextIv = binding.player.findViewById<ImageView>(R.id.nextIv)

        prevIv.setOnClickListener {
            currentId?.let { id -> setPreviousLesson(id) }
        }

        nextIv.setOnClickListener {
            currentId?.let { id -> setNextLesson(id) }
        }
    }

    private fun subscribeToFlow() {
        if (currentOrientation == Orientation.PORTRAIT) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.lessons.collect { items: List<Lesson> ->
                        lessonsAdapter?.submitList(items)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentLesson.collect { lesson: Lesson ->
                    currentId = lesson.id
                    play(lesson.videoUrl)
                    with(binding) {
                        titleTv?.text = lesson.name
                        descriptionTv?.text = lesson.description
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { loading: Boolean ->
                    binding.lessonsLayout?.let {
                        if (loading) {
                            it.lessonsRv.isVisible = false
                            it.simmerContainer.isVisible = true
                            it.placeholderRv.adapter = PlaceholderAdapter()
                            it.simmerContainer.startShimmer()
                        } else {
                            viewModel.videoState.id?.let { id -> viewModel.getLessonById(id) }
                            it.simmerContainer.stopShimmer()
                            it.placeholderRv.adapter = null
                            it.lessonsRv.isVisible = true
                            it.simmerContainer.isVisible = false
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showDialog.collect {
                    if (it) {
//                        PurchaseFragmentDialog.newInstance()
                    }
                }
            }
        }
    }

    private fun initPlayer() {
        videoPlayer = ExoPlayer.Builder(requireContext())
            .setSeekBackIncrementMs(5000L)
            .setSeekForwardIncrementMs(5000L)
            .build()

        with(binding.player) {
            player = videoPlayer
            keepScreenOn = true
        }
    }

    private fun play(videoSource: String) {
        val mediaItem = MediaItem.fromUri(videoSource)

        videoPlayer!!.apply {
            setMediaItem(mediaItem)
            seekTo(viewModel.videoState.currentItem, viewModel.videoState.playbackPosition)
            prepare()
        }

        videoPlayer?.play()
    }

    private fun bindAdapter() {
        lessonsAdapter = LessonsAdapter { id: Int, isOpen ->
            if (isOpen) {
                viewModel.videoState = VideoState(
                    0,
                    0,
                    id,
                    currentOrientation
                )
                viewModel.getAllLessons(id)
                viewModel.getLessonById(id)
            } else {
                PurchaseFragmentDialog.newInstance(id)
                    .show(childFragmentManager, PurchaseFragmentDialog.TAG)
            }
        }
        binding.lessonsLayout?.let {
            with(it.lessonsRv) {
                setHasFixedSize(true)
                addItemDecoration(LessonsItemDecorator())
                adapter = lessonsAdapter
            }
        }
    }

    private fun releasePlayer() {
        videoPlayer?.let { exoPlayer ->
            viewModel.videoState = VideoState(
                exoPlayer.currentMediaItemIndex,
                exoPlayer.currentPosition,
                currentId,
                currentOrientation
            )
            exoPlayer.release()
        }
        videoPlayer = null
    }

    private fun setNextLesson(currentId: Int) {
        val list = viewModel.list
        list.forEachIndexed { index, lesson ->
            if (lesson.id == currentId) {
                val nextIndex = index + 1
                if (nextIndex < list.size) {
                    val nextLesson = list[nextIndex]
                    if (nextLesson.isOpen) {
                        viewModel.setNextLesson(nextLesson)
                    } else {
                        showDialog(nextLesson.id)
                    }
                }
                return@forEachIndexed
            }
        }
    }

    private fun setPreviousLesson(currentId: Int) {
        val list = viewModel.list
        list.forEachIndexed { index, lesson ->
            if (lesson.id == currentId) {
                val prevIndex = index - 1
                if (prevIndex >= 0) {
                    val prevLesson = list[prevIndex]
                    Log.e("TAG", "setPreviousLesson: ${prevLesson.isOpen}")
                    if (prevLesson.isOpen) {
                        viewModel.setPreviousLesson(prevLesson)
                    } else {
                        showDialog(prevLesson.id)
                    }
                }
                return@forEachIndexed
            }
        }
    }

    private fun showDialog(id: Int) {
        PurchaseFragmentDialog.newInstance(id)
            .show(childFragmentManager, PurchaseFragmentDialog.TAG)
    }
}