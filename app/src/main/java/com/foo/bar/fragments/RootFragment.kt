package com.foo.bar.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.transition.Slide
import androidx.transition.Fade
import com.foo.bar.R
import com.foo.bar.ext.ColorUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.MaterialSharedAxis

class RootFragment(private val light: Boolean) : Fragment() {
    private lateinit var containerView: CoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var linearLayout: LinearLayout

    constructor() : this(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade(Fade.IN)
        exitTransition = Fade(Fade.OUT)
//        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
//        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        // Linear layout with few buttons allowing for basic navigation
        initializeContainerView()
        setBackgroundColor()

        return containerView
    }

    private fun initializeContainerView() {
        containerView = CoordinatorLayout(requireContext()).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
            )
        }
        appBarLayout = AppBarLayout(requireContext()).apply {
            addView(createToolbar())
            layoutParams = AppBarLayout.LayoutParams(
                AppBarLayout.LayoutParams.MATCH_PARENT,
                AppBarLayout.LayoutParams.WRAP_CONTENT
            )
        }
        containerView.addView(appBarLayout)

        linearLayout = LinearLayout(requireContext()).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT
            ).apply {
//                anchorId = appBarLayout.id
//                anchorGravity = Gravity.BOTTOM
                gravity = Gravity.CENTER_VERTICAL
            }
            orientation = LinearLayout.VERTICAL
        }
        createNavigationButtons().forEach { linearLayout.addView(it) }

        linearLayout.addView(createMosaicTile(requireContext()))

        containerView.addView(linearLayout)
    }

    private fun createMosaicTile(context: Context): View {
        return View(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200
            ).apply {
                bottomMargin = 50
            }
            setBackgroundColor(ColorUtils.randomColor())
            elevation = 24f
        }
    }

    private fun setBackgroundColor() {
        val color = requireContext().resources.getColor(
            if (light) R.color.colorPrimary else R.color.colorSecondary,
            requireContext().theme
        )
        val colorContainer = requireContext().resources.getColor(
            if (light) R.color.colorPrimaryContainer else R.color.colorSecondaryContainer,
            requireContext().theme
        )

        containerView.setBackgroundColor(colorContainer)
        appBarLayout.setBackgroundColor(color)
    }

    private fun createNavigationButtons(): List<Button> {
        val res = requireContext().resources
        val navForwardButton = createButton(res.getString(R.string.open_fragment)) {
            Log.i(TAG, "navForwardButton onClickListener")
            navigateToFragment(createNewRootFragment())
        }
        val openModalSheetButton = createButton(res.getString(R.string.open_modal_button)) {
            Log.i(TAG, "openModalSheetButton onClickListener")
            showModalWithExplicitFragmentTransaction(ModalBottomSheet())
        }
        val openStandardSheetButton = createButton(res.getString(R.string.open_sheet_button)) {
            Log.i(TAG, "openStandardSheetButton onClickListener")
            showStandardSheetWithExplicitFragmentTransaction(DimmedStandardBottomSheet())
//            showStandardSheetWithExplicitFragmentTransaction(StandardBottomSheet())
        }
        val popFragmentButton = createButton(res.getString(R.string.pop_fragment)) {
            Log.i(TAG, "popFragmentButton onClickListener")
            removeFragmentFromStack(this)
        }
        val replaceContentWithSnapshotButton =
            createButton(res.getString(R.string.replace_content)) {
                Log.i(TAG, "replaceContentWithSnapshotButton onClickListener")
                val bitmap = linearLayout.drawToBitmap()
                val snapshot = ImageView(context)
                snapshot.setImageBitmap(bitmap)

                containerView.removeView(linearLayout)
                containerView.addView(snapshot)
            }
        val showRegularFormSheetButton = createButton(res.getString(R.string.regular_formsheet_fragment)) {
            Log.i(TAG, "showRegularFormSheetButton onClickListener")
            navigateToRegularFormSheet()
        }
        return if (parentFragmentManager.fragments.size == 1) {
            listOf(
                navForwardButton,
                openModalSheetButton,
                openStandardSheetButton,
                replaceContentWithSnapshotButton,
                showRegularFormSheetButton
            )
        } else {
            listOf(
                navForwardButton,
                openModalSheetButton,
                openStandardSheetButton,
                popFragmentButton,
                replaceContentWithSnapshotButton,
                showRegularFormSheetButton
            )
        }
    }

    private fun createButton(text: String, onClickListener: View.OnClickListener): Button {
        Log.i(TAG, "createButton")
        val button = Button(context).apply {
            layoutParams = AppBarLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = text
            this.elevation = 40f
            setOnClickListener(onClickListener)
        }
        return button
    }

    private fun createToolbar(): MaterialToolbar {
        val toolbar = MaterialToolbar(requireContext())
        toolbar.layoutParams = AppBarLayout.LayoutParams(
            AppBarLayout.LayoutParams.MATCH_PARENT,
            AppBarLayout.LayoutParams.WRAP_CONTENT,
        )
        toolbar.title = requireContext().resources.getString(R.string.root_fragment_name)
        return toolbar
    }

    private fun createNewRootFragment(): RootFragment = RootFragment(!light)

    private fun navigateToFragment(fragment: Fragment) {
        Log.i(TAG, "Navigating to next Fragment")
        parentFragmentManager.commitNow(allowStateLoss = true) {
            setReorderingAllowed(true)
//            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(R.id.fragment_container_view, fragment, null)
        }
    }

    private fun navigateToRegularFormSheet() {
        Log.i(TAG, "Navigate to regular FormSheet")
        parentFragmentManager.commitNow(allowStateLoss = true) {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, FormSheetV4Fragment(), null)
        }
    }

    private fun showModalWithExplicitFragmentTransaction(fragment: Fragment) {
        Log.i(TAG, "Opening modal fragment")
        parentFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            add(fragment, null)
        }
    }

    private fun showStandardSheetWithExplicitFragmentTransaction(fragment: Fragment) {
        Log.i(TAG, "Opening standard sheet fragment")
        parentFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
//            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // Note that in case of standard bottom sheet, it must be added to container view...
            add(R.id.fragment_container_view, fragment, "Sheet")
        }
    }

    private fun removeFragmentFromStack(fragment: Fragment) {
        Log.i(TAG, "Popping fragment")
        if (parentFragmentManager.fragments.size == 1) {
            Log.i(TAG, "Popping won't be performed as last fragment can't be popped")
            return
        }
        parentFragmentManager.commitNow(allowStateLoss = true) {
            setReorderingAllowed(true)
//            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            remove(fragment)
        }
    }

    companion object {
        const val TAG = "RootFragment"
    }
}