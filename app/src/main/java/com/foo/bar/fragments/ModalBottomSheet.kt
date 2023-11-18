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

class ModalBottomSheet(contentView: View? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentModalBinding
    private var contentView = contentView

    constructor() : this(null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i(TAG, "onCreateDialog")
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setContentView(ensureContentView)
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
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

    private val ensureContentView: View
        get() {
            if (contentView == null) {
                contentView = createContentView()
            }
            return contentView!!
        }

    companion object {
        const val TAG = "Modal"
    }
}