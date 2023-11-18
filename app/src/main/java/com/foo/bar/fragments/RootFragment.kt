package com.foo.bar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.foo.bar.R

class RootFragment : Fragment() {
    private lateinit var containerView: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Linear layout with few buttons allowing for basic navigation
        containerView = createContainerView()

        val buttons = createNavigationButtons()

        for (button in buttons) {
            containerView.addView(button)
        }

        // TODO: Should I do it?
//        container?.addView(containerView)

        return containerView
    }

    private fun createContainerView(): LinearLayout = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    private fun createNavigationButtons(): List<Button> {
        val navForwardButton = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
               Log.i(TAG, "navForwardButton OnClickListener")
            }
            text = context.resources.getString(R.string.open_fragment)
        }

        return listOf(navForwardButton)
    }

    companion object {
        const val TAG = "RootFragment"
    }
}