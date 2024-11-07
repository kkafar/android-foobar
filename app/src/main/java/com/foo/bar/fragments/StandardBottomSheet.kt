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
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setPadding
import androidx.core.widget.NestedScrollView
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

//    private lateinit var scrollableContentView: ScrollView
    private lateinit var scrollableContentView: NestedScrollView

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return AnimationUtils.loadAnimation(
            context, if (enter) R.anim.slide_in_bottom else R.anim.slide_out_bottom
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        initializeViewHierarchy()
//        container?.addView(containerView)
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")

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

    private fun initializeViewHierarchy() {
        initializeContainerView()
        initializeBottomSheet()
        initializeContentView()
        initializeScrollableContentView()

//        bottomSheet.addView(contentView)
        bottomSheet.addView(scrollableContentView)

        containerView.addView(bottomSheet)

//        bottomSheet.clipChildren = true
        bottomSheet.clipToOutline = true
//        bottomSheet.clipToPadding = true

        attachShapeToView(bottomSheet, cornerSize = 40f)
//        bottomSheet.setPadding(0, 40, 0, 0)

//        attachShapeToView(contentView, cornerSize = 40f)
//        contentView.setPadding(0, 20, 0, 0)

//        containerView.elevation = 10f
        bottomSheet.elevation = 40f

//        contentView.elevation = 20f
//
    }

    private fun initializeScrollableContentView() {
//        scrollableContentView = ScrollView(requireContext()).apply {
//            addView(contentView)
//            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
//            isNestedScrollingEnabled = true
//            isEnabled
//        }

        scrollableContentView = NestedScrollView(requireContext()).apply {
            addView(contentView)
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            isNestedScrollingEnabled = true
            isEnabled
        }
    }

    private fun initializeContainerView() {
        containerView = CoordinatorLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            setBackgroundColor(Color.TRANSPARENT)
//            setBackgroundColor(Color.argb(0.5F, 0F, 0F, 0F))
//            alpha = 1.0F
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
        bottomSheet.setBackgroundColor(
            requireContext().resources.getColor(
                R.color.colorSecondary, requireContext().theme
            )
        )
    }

    private fun initializeContentView() {
        contentView = LinearLayout(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
        }

        createButtonsForContentView(n = 10).forEach {
            contentView.addView(it)
        }
//        contentView.setBackgroundColor(requireContext().resources.getColor(R.color.colorOnSecondary, requireContext().theme))
        contentView.setBackgroundColor(Color.WHITE)
//        contentView.setPadding(40)
//        contentView.elevation = 12f
    }

    private fun createButtonsForContentView(n: Int = 10): List<Button> {
        val res = requireContext().resources
        return generateSequence {
            createButton(res.getString(R.string.pop_fragment)) {
                Log.i(TAG, "Popping StandardBottomSheet")
            }
        }.take(n).toList()
    }

    private fun createButton(text: String, onClickListener: View.OnClickListener): Button {
        Log.i(RootFragment.TAG, "createButton")
        val button = Button(context).apply {
            layoutParams = AppBarLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = text
            setOnClickListener(onClickListener)
        }
        return button
    }

    private fun attachShapeToView(view: View, cornerSize: Float = 20f) {
        val shapeAppearanceModel = ShapeAppearanceModel.Builder().apply {
            setTopLeftCorner(CornerFamily.ROUNDED, cornerSize)
            setTopRightCorner(CornerFamily.ROUNDED, cornerSize)
        }.build()
        val shape = MaterialShapeDrawable(shapeAppearanceModel)
        shape.setTint(Color.WHITE)
        view.background = shape
    }

    companion object {
        const val TAG = "StandardBottomSheet"
    }
}