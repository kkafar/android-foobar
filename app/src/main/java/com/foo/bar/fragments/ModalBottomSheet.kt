package com.foo.bar.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foo.bar.databinding.FragmentModalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(val contentView: View? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentModalBinding

    constructor() : this(null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setContentView(contentView ?: createContentView())
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
    }

    private fun createContentView(): View {
        val view = View(requireContext())
        view.setBackgroundColor(Color.DKGRAY)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setBackgroundColor(Color.DKGRAY)
        return view
    }

    companion object {
        const val TAG = "Modal"
    }
}