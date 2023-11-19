package com.foo.bar.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.foo.bar.databinding.FragmentModalBinding
import com.foo.bar.ext.parentAsView
import com.foo.bar.ext.parentAsViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(contentView: View? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentModalBinding
    private var contentView = contentView

    constructor() : this(null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i(TAG, "onCreateDialog")
        val bottomSheetDialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.apply {
            behavior.apply {
                halfExpandedRatio = 0.3F
//                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            setCanceledOnTouchOutside(false)
            dismissWithAnimation = true
        }
//        bottomSheetDialog.setContentView(ensureContentView)
//        disableDimmingView()
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
//        return null;
//        return createContentView()
        return ensureContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        ensureContentView.parentAsView()?.clipToOutline = true

//        disableDimmingView()
    }

    private fun createContentView(): View {
//        val linearLayout = LinearLayout

        val view = View(requireContext())
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setBackgroundColor(Color.DKGRAY)
        view.clipToOutline = true
        return view
    }

    private fun disableDimmingView() {
        Log.i(TAG, "Disabling dimming view")
        ensureContentView.parentAsView()?.parentAsViewGroup()?.removeViewAt(0)
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