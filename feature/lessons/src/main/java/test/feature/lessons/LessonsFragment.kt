package test.feature.lessons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import test.core.model.Lesson
import test.core.ui.PurchaseFragmentDialog
import test.core.ui.adapter.LessonsAdapter
import test.core.ui.adapter.LessonsItemDecorator
import test.core.ui.adapter.PlaceholderAdapter
import test.feature.lessons.databinding.FragmentLessonsBinding
import test.feature.lessons.navigation.LessonsDirections

@AndroidEntryPoint
class LessonsFragment : Fragment() {

    private var _binding: FragmentLessonsBinding? = null
    private val binding: FragmentLessonsBinding get() = requireNotNull(_binding)

    private val viewModel by viewModels<LessonsViewModel>()

    private var lessonsAdapter: LessonsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonsBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lessons.collect { items: List<Lesson> ->
                    Log.e("TAG", "onViewCreated: ${items.map { it.isOpen }}")
                    lessonsAdapter?.submitList(items)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    with(binding) {
                        if (it) {
                            lessonsLayout.lessonsRv.isVisible = false
                            lessonsLayout.simmerContainer.isVisible = true
                            lessonsLayout.placeholderRv.adapter = PlaceholderAdapter()
                            lessonsLayout.simmerContainer.startShimmer()
                        } else {
                            lessonsLayout.simmerContainer.stopShimmer()
                            lessonsLayout.placeholderRv.adapter = null
                            lessonsLayout.lessonsRv.isVisible = true
                            lessonsLayout.simmerContainer.isVisible = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        lessonsAdapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun bindAdapter() {
        lessonsAdapter = LessonsAdapter { id: Int, isOpen: Boolean ->
            if (isOpen) {
                findDependencies().navigate(LessonsDirections.ToVideoPlayerFragment(id))
            } else {
                PurchaseFragmentDialog.newInstance(id)
                    .show(childFragmentManager, PurchaseFragmentDialog.TAG)
            }
        }
        with(binding.lessonsLayout.lessonsRv) {
            setHasFixedSize(true)
            addItemDecoration(LessonsItemDecorator())
            adapter = lessonsAdapter
        }
    }
}