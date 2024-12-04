package com.foo.bar.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionSet
import androidx.transition.Visibility
import com.foo.bar.FragmentDismissalDelegate
import com.foo.bar.views.CustomCoordinatorLayout
import com.foo.bar.views.DimmingView
import com.foo.bar.views.SheetView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class FormSheetV4Fragment : Fragment(), FragmentDismissalDelegate {
    // Container
    private lateinit var containerView: CoordinatorLayout
    private lateinit var dimmingView: DimmingView
    private lateinit var sheetView: SheetView

    private val dismissalCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            Log.i(TAG, "state changed to $newState, hidden is: ${BottomSheetBehavior.STATE_HIDDEN}")
            // For some reason state hidden is skipped in favour of collapsed for now.
            // Figure this out!
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismissSelf()
            }
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissSelf()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewHierarchy()
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTransitions()
    }

    private fun initViewHierarchy() {
        val context = requireNotNull(context) { "Context is null while initializing hierarchy" }
        dimmingView = createDimmingView(context)
        containerView = createContainerView(context)
        sheetView = createSheetView(context)

        // Dimming view as first child
        containerView.addView(dimmingView, 0)

        // Sheet as second child
        containerView.addView(sheetView, 1)
    }

    private fun createDimmingView(context: Context) = DimmingView(context).apply {
        layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )
    }

    private fun createContainerView(context: Context) = CustomCoordinatorLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun createSheetView(context: Context) = SheetView(context, this).apply {
        layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )
        castedLayoutParams.apply {
            behavior = BottomSheetBehavior<SheetView>().apply {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                skipCollapsed = true
                addBottomSheetCallback(dismissalCallback)
            }
        }
    }

    private fun configureTransitions() {
        enterTransition = TransitionSet().apply {
            addTransition(Fade(Visibility.MODE_IN).apply {
                addTarget(dimmingView)
            })
            addTransition(Slide(Gravity.BOTTOM).apply {
                excludeTarget(dimmingView, true)
            })
            duration = 500
        }

        exitTransition = TransitionSet().apply {
            addTransition(Fade(Visibility.MODE_OUT).apply {
                addTarget(dimmingView)
            })
            addTransition(Slide(Gravity.BOTTOM).apply {
                addTarget(sheetView)
            })
            duration = 500
        }
    }

    private fun dismissSelf() {
        parentFragmentManager.commitNow(allowStateLoss = true) {
            setReorderingAllowed(true)
            remove(this@FormSheetV4Fragment)
        }
    }

    override fun dismissFragment() {
        dismissSelf()
    }

    companion object {
        const val TAG = "FormSheetV4Fragment"
    }
}