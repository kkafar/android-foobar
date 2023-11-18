package com.foo.bar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.foo.bar.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var openModalButton: Button
    private lateinit var openContentButton: Button
    private lateinit var toolbar: MaterialToolbar

    private lateinit var layout: CoordinatorLayout
    private lateinit var appbarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        openModalButton = mainBinding.openButton
        toolbar = mainBinding.toolbar
        openContentButton = mainBinding.openFragmentButton

        toolbar.apply {
            title = "AppTitle"
            isTitleCentered = true
            setBackgroundColor(Color.LTGRAY)
        }

        openModalButton.setOnClickListener {
//            this.createAndShowModal()
//            attachSomeContent()
            createAndShowModalWithFragmentTransaction()
        }

        openContentButton.setOnClickListener {
//            val contentFragment = ContentFragment()
//            Log.i(TAG, "onClickListener for openContentFragment")
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                add(contentFragment, "ContentFragmentTag")
//            }
        }

        setContentView(mainBinding.root)

//        layout = CoordinatorLayout(this)
//        layout.layoutParams = CoordinatorLayout.LayoutParams(
//            CoordinatorLayout.LayoutParams.MATCH_PARENT,
//            CoordinatorLayout.LayoutParams.MATCH_PARENT
//        )
//
//        appbarLayout = AppBarLayout(this)
//        appbarLayout.layoutParams = AppBarLayout.LayoutParams(
//            AppBarLayout.LayoutParams.MATCH_PARENT,
//            AppBarLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        toolbar = MaterialToolbar(this).apply {
//            title = "AppTitle"
//            isTitleCentered = true
//        }
//
//        appbarLayout.addView(toolbar)
//        layout.addView(appbarLayout)
//
//        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun createAndShowModalWithFragmentTransaction() {
        val modalBottomSheet = ModalBottomSheet(createContentView())
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(modalBottomSheet, ModalBottomSheet.TAG)
        }
    }

    private fun createAndShowModal() {
        val modalBottomSheet = ModalBottomSheet()
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
    }

    private fun attachSomeContent() {
//       mainBinding.root.addView()
        mainBinding.root.addView(
            createModalViewContent(), ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        mainBinding.root.requestLayout()
    }

    private fun createModalViewContent(): View {
        val coordinatorLayout = CoordinatorLayout(this)
        val frameLayout = FrameLayout(this)
        val view = View(this)

        view.setBackgroundColor(Color.GREEN)
        frameLayout.setBackgroundColor(Color.BLUE)
//        coordinatorLayout.setBackgroundColor(Color.YELLOW)

        frameLayout.addView(
            view,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        )

        coordinatorLayout.addView(
            frameLayout, CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
            )
        )

        val frameLayoutParams = frameLayout.layoutParams as CoordinatorLayout.LayoutParams
        frameLayoutParams.behavior = BottomSheetBehavior<FrameLayout>()

        val behavior = BottomSheetBehavior.from(frameLayout)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        coordinatorLayout.requestLayout()


        return coordinatorLayout
    }

    private fun createContentView(): View {
        val view = View(this)
        view.setBackgroundColor(Color.DKGRAY)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setBackgroundColor(Color.DKGRAY)
        return view
    }
}