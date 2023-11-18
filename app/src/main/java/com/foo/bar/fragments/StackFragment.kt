package com.foo.bar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class StackFragment : Fragment() {
    lateinit var layout: CoordinatorLayout
    lateinit var appbarLayout: AppBarLayout
    lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = CoordinatorLayout(requireContext())
        layout.layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT)

        appbarLayout = AppBarLayout(requireContext())
        appbarLayout.layoutParams = AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT)

        toolbar = MaterialToolbar(requireContext())

        appbarLayout.addView(toolbar)
        layout.addView(appbarLayout)

        return layout
    }

}