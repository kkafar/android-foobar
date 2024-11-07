package com.foo.bar.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.children
import com.foo.bar.databinding.FragmentModalBinding
import com.foo.bar.ext.parentAsView
import com.foo.bar.ext.parentAsViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class ModalBottomSheet(contentView: View? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentModalBinding
    private var contentView = contentView
    private var containerView: ViewGroup? = null

    constructor() : this(null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i(TAG, "onCreateDialog")
        val bottomSheetDialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.apply {
            behavior.apply {
                halfExpandedRatio = 0.5F
                state = BottomSheetBehavior.STATE_COLLAPSED
                isFitToContents = false
                skipCollapsed = false
                peekHeight = 800
            }
            setCanceledOnTouchOutside(false)
            dismissWithAnimation = true
        }
//        bottomSheetDialog.setContentView(ensureContentView)
//        disableDimmingView()
//        bottomSheetDialog.window?.setDimAmount(0F)
//        bottomSheetDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        bottomSheetDialog.window?.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
//        return null;
        ensureContainerView.addView(createEditText())
        ensureContainerView.addView(ensureContentView)
        return ensureContainerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        ensureContainerView.parentAsView()?.clipToOutline = true
        ensureContainerView.clipToOutline = true

//        disableDimmingView()
    }

    private fun createContentView(): View {
        val view = View(requireContext())
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setBackgroundColor(Color.GRAY)

        return view
    }

    private fun createEditText(): EditText {
        val editText = TextInputEditText(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        }
        return editText
    }

    private fun createContainerView(): ViewGroup {
        val view = FrameLayout(requireContext())
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setBackgroundColor(Color.GRAY)

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

    private val ensureContainerView: ViewGroup
        get() {
            if (containerView == null) {
                containerView = createContainerView()
            }
            return containerView!!
        }

    companion object {
        const val TAG = "Modal"
    }
}