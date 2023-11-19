package com.foo.bar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.foo.bar.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class StandardBottomSheet : Fragment() {
    private lateinit var containerView: CoordinatorLayout
    private lateinit var contentView: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        initializeViewHierarchy()
//        container?.addView(containerView)
        return containerView
    }

    private fun initializeViewHierarchy() {
        initializeContainerView()
        initializeContentView()
        containerView.addView(contentView)
    }

    private fun initializeContainerView() {
        containerView = CoordinatorLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    }

    private fun initializeContentView() {
        contentView = FrameLayout(requireContext()).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
            ).apply {
                behavior = BottomSheetBehavior<FrameLayout>().apply {
                    peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
                    state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    isDraggable = true
                    isHideable = true
                }
            }
        }
        contentView.setBackgroundColor(requireContext().resources.getColor(R.color.colorSecondary, requireContext().theme))
    }

    companion object {
        const val TAG = "StandardBottomSheet"
    }
}