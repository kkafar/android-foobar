package com.foo.bar

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foo.bar.databinding.FragmentModalBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Modal : BottomSheetDialogFragment() {
    lateinit var binding: FragmentModalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = BottomSheetBehavior.from(view.parent as View)
        Log.i(TAG, "maxHeight initial value ${behavior.maxHeight}")
        behavior.apply {
            isDraggable = true
//            maxHeight = 1000
            peekHeight = 900
        }

        binding.expandButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.halfExpandedButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.collapsedButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.hiddenButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    companion object {
        const val TAG = "Modal"
    }
}