package test.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import test.core.ui.databinding.FragmentPurchaseDialogBinding

@AndroidEntryPoint
class PurchaseFragmentDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentPurchaseDialogBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<PurchaseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseDialogBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.payBtn.setOnClickListener {
            arguments?.getInt("id")?.let { id -> viewModel.saveLesson(id) }
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "PurchaseFragmentDialog"

        fun newInstance(id: Int): PurchaseFragmentDialog {
            return PurchaseFragmentDialog().also {
                it.arguments = bundleOf("id" to id)
            }
        }
    }
}