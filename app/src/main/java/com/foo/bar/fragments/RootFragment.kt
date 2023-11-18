package com.foo.bar.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.foo.bar.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class RootFragment : Fragment() {
    private lateinit var containerView: CoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
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
        val res = requireContext().resources
        val navForwardButton = createButton(res.getString(R.string.open_fragment)) {
            Log.i(TAG, "navForwardButton onClickListener")
            navigateToFragment(createNewRootFragment())
        }
        val openStandardModalButton = createButton(res.getString(R.string.open_modal_button)) {
            Log.i(TAG, "openStandardModalButton onClickListener")
            showModalWithExplicitFragmentTransaction(ModalBottomSheet())
        }
        val popFragmentButton = createButton(res.getString(R.string.pop_fragment)) {
            Log.i(TAG, "popFragmentButton onClickListener")
            removeFragmentFromStack(this)
        }
        return listOf(navForwardButton, openStandardModalButton, popFragmentButton)
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

    private fun createNewRootFragment(): RootFragment = RootFragment()

    private fun navigateToFragment(fragment: Fragment) {
        Log.i(TAG, "Navigating to next Fragment")
        parentFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(R.id.fragment_container_view, fragment, null)
        }
    }

    private fun showModalWithExplicitFragmentTransaction(fragment: Fragment) {
        Log.i(TAG, "Opening modal fragment")
        parentFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            add(fragment, null)
        }
    }

    private fun removeFragmentFromStack(fragment: Fragment) {
        Log.i(TAG, "Popping fragment")
        if (parentFragmentManager.fragments.size == 1) {
            Log.i(TAG, "Popping won't be performed as last fragment can't be popped")
            return
        }
        parentFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            remove(fragment)
        }
    }

    companion object {
        const val TAG = "RootFragment"
    }
}