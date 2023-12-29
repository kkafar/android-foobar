package com.foo.bar.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.foo.bar.R

class DimmedStandardBottomSheet : Fragment(), LifecycleEventObserver, Animation.AnimationListener {
    private lateinit var dimmingView: CoordinatorLayout
    private lateinit var containerView: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {

        return AnimationUtils.loadAnimation(context, if (enter) R.anim.fade_in else R.anim.fade_out)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(StandardBottomSheet.TAG, "onCreateView")
        initializeContainerView()
        initializeDimmingView()

        containerView.addView(dimmingView)
        return containerView
    }


    private fun initializeDimmingView() {
        dimmingView = CoordinatorLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.BLACK)
            alpha = 0.4F
            id = View.generateViewId()
        }
    }

    private fun initializeContainerView() {
        containerView = FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            setBackgroundColor(Color.TRANSPARENT)
            id = View.generateViewId()
        }
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        childFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            add(requireView().id, createBottomSheetFragment(), "StandardBottomSheet")
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun createBottomSheetFragment(): StandardBottomSheet {
        val sheetFragment = StandardBottomSheet()
        sheetFragment.lifecycle.addObserver(this)
        return sheetFragment
    }

    companion object {
        const val TAG = "DimmedStandardBottomSheet"
    }

    private fun dismissWithAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        animation.setAnimationListener(this)
        requireView().startAnimation(animation)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                Log.i(TAG, "received ON_STOP")
                source.lifecycle.removeObserver(this@DimmedStandardBottomSheet)
                dismissWithAnimation()
            }
            else -> {}
        }
    }

    override fun onAnimationStart(animation: Animation?) = Unit

    override fun onAnimationEnd(animation: Animation?) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            remove(this@DimmedStandardBottomSheet)
        }
    }

    override fun onAnimationRepeat(animation: Animation?) = Unit

    
}