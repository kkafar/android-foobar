package com.foo.bar.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.commit
import com.foo.bar.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class StandardBottomSheet : Fragment() {
    private lateinit var containerView: CoordinatorLayout
    private lateinit var bottomSheet: ViewGroup
    private lateinit var contentView: ViewGroup

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return AnimationUtils.loadAnimation(context, if (enter) R.anim.slide_in_bottom else R.anim.slide_out_bottom)
    }

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

        bottomSheet.clipChildren = true
        bottomSheet.clipToOutline = true
        attachShapeToView(bottomSheet)
    }

    private fun initializeContainerView() {
        containerView = CoordinatorLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
//            setBackgroundColor(Color.BLACK)
//            setBackgroundColor(Color.argb(0.5F, 0F, 0F, 0F))
            alpha = 1.0F
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
                }.also {
                    it.addBottomSheetCallback(object : BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                parentFragmentManager.commit {
                                    setReorderingAllowed(true)
                                    remove(this@StandardBottomSheet)
                                }
                            }
                        }
                        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
                    })
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

    private fun attachShapeToView(view: View) {
        val shapeAppearanceModel = ShapeAppearanceModel.Builder().apply {
            setTopLeftCorner(CornerFamily.ROUNDED, 20F)
            setTopRightCorner(CornerFamily.ROUNDED, 20F)
        }.build()
        val shape = MaterialShapeDrawable(shapeAppearanceModel)
        view.background = shape;
    }

    companion object {
        const val TAG = "StandardBottomSheet"
    }
}