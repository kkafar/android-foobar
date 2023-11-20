package com.foo.bar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.foo.bar.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class StandardBottomSheet : Fragment() {
    private lateinit var containerView: CoordinatorLayout
    private lateinit var bottomSheet: ViewGroup
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
        initializeBottomSheet()
        initializeContentView()

        bottomSheet.addView(contentView)
        containerView.addView(bottomSheet)

    }

    private fun initializeContainerView() {
        containerView = CoordinatorLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    }

    private fun initializeBottomSheet() {
        bottomSheet = FrameLayout(requireContext()).apply {
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
//        bottomSheet.setBackgroundColor(requireContext().resources.getColor(R.color.colorSecondary, requireContext().theme))
    }

    private fun initializeContentView() {
        contentView = LinearLayout(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
        }

        createButtonsForContentView().forEach {
            contentView.addView(it)
        }
        contentView.setBackgroundColor(requireContext().resources.getColor(R.color.colorSecondary, requireContext().theme))
    }

    private fun createButtonsForContentView(): List<Button> {
        val res = requireContext().resources
        return listOf(
           createButton(res.getString(R.string.pop_fragment)) {
               Log.i(TAG, "Popping StandardBottomSheet")
           }
        )
    }

    private fun createButton(text: String, onClickListener: View.OnClickListener): Button {
        Log.i(RootFragment.TAG, "createButton")
        val button = Button(context).apply {
            layoutParams = AppBarLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = text
            setOnClickListener(onClickListener)
        }
        return button
    }

    companion object {
        const val TAG = "StandardBottomSheet"
    }
}