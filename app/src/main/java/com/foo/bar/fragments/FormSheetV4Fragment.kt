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
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commitNow
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionSet
import androidx.transition.Visibility
import com.foo.bar.FragmentDismissalDelegate
import com.foo.bar.ext.disassembleSubTreeRootedIn
import com.foo.bar.ext.endViewTransitionRecursively
import com.foo.bar.ext.parentAsViewGroup
import com.foo.bar.ext.startViewTransitionRecursively
import com.foo.bar.views.CustomCoordinatorLayout
import com.foo.bar.views.DimmingView
import com.foo.bar.views.SheetView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class FormSheetV4Fragment : Fragment(), FragmentDismissalDelegate {
    // Container
    private lateinit var containerView: CoordinatorLayout // corresponds to ScreensCoordinatorLayout
    private lateinit var dimmingView: DimmingView // corresponds to dimming view
    private lateinit var sheetView: SheetView // corresponds to screen

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
        }.addListener(object : Transition.TransitionListener {
            private var cancelled = false
            private var started = false

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                if (!cancelled) {
                    cancelled = true
                    Log.i(TAG, "onTransitionEnd")
                    endRemovalTransition()
                }
            }

            override fun onTransitionStart(transition: Transition) {
                if (!started) {
                    started = true
                    Log.i(TAG, "onTransitionStart")
                }
            }
            override fun onTransitionCancel(transition: Transition) = Unit
            override fun onTransitionPause(transition: Transition) = Unit
            override fun onTransitionResume(transition: Transition) = Unit

        })
    }

    private fun dismissSelf() {
        parentFragmentManager.commitNow(allowStateLoss = true) {
            setReorderingAllowed(true)
            remove(this@FormSheetV4Fragment)
        }
    }

    override fun dismissFragment() {
        if (!MIMIC_RN_DISMISS) {
            dismissSelf()
        } else {
            dismissMimickingReactNativeSetup()
        }
    }

    private fun dismissMimickingReactNativeSetup() {
        // Step 1 - mark all views as transitioning
        startRemovalTransition()

        // Step 2 - disassemble the view hierarchy from container down
        disassembleViewHierarchyBottomUpPhaseOne()

        // Step 3 - Mimic update on Screen Stack
        dismissSelf()

        // Step 4 - disassemble the view hierarchy from fragment container down (screen stack down)
        disassembleViewHierarchyBottomUpPhaseTwo()
    }

    private fun startRemovalTransition() {
        val fragmentContainer = containerView.parentAsViewGroup()
        assert(fragmentContainer is FragmentContainerView)

        fragmentContainer!!.startViewTransitionRecursively(containerView)
    }

    private fun endRemovalTransition() {
        val fragmentContainer = containerView.parentAsViewGroup()
        assert(fragmentContainer is FragmentContainerView)

        fragmentContainer!!.endViewTransitionRecursively(containerView)
    }

    private fun disassembleViewHierarchyBottomUpPhaseOne() {
        // Disassemble everything under screen
        sheetView.disassembleSubTreeRootedIn(sheetView.contentView)
    }

    private fun disassembleViewHierarchyBottomUpPhaseTwo() {
        val fragmentContainer = containerView.parentAsViewGroup()
        assert(fragmentContainer is FragmentContainerView)

        fragmentContainer!!.disassembleSubTreeRootedIn(containerView)

    }

    companion object {
        const val TAG = "FormSheetV4Fragment"

        const val MIMIC_RN_DISMISS = true
    }
}