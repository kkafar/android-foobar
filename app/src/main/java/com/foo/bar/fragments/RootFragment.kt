package com.foo.bar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.foo.bar.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class RootFragment : Fragment() {
    private lateinit var containerView: CoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Linear layout with few buttons allowing for basic navigation
        initializeContainerView()

        val buttons = createNavigationButtons()

        for (button in buttons) {
            appBarLayout.addView(button)
        }

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
    }

    private fun createNavigationButtons(): List<Button> {
        val navForwardButton = createButton(requireContext().resources.getString(R.string.open_fragment)) {
            Log.i(TAG, "navForwardButton OnClickListener")
        }
        val openStandardBottomSheetButton = createButton(requireContext().resources.getString(R.string.open_modal_button)) {
            Log.i(TAG, "openStandardBottomSheetButton OnClickListener")
        }
        val popFragmentButton = createButton(requireContext().resources.getString(R.string.pop_fragment)) {
            Log.i(TAG, "popFragmentButton OnClickListener")
        }

        return listOf(navForwardButton, openStandardBottomSheetButton, popFragmentButton)
    }

    private fun createButton(text: String, onClickListener: View.OnClickListener): Button {
        val button = Button(context).apply {
            layoutParams = AppBarLayout.LayoutParams(
                AppBarLayout.LayoutParams.MATCH_PARENT,
                AppBarLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = text
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

    companion object {
        const val TAG = "RootFragment"
    }
}