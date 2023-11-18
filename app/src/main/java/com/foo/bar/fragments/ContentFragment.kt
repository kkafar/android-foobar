package com.foo.bar.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foo.bar.databinding.FragmentContentBinding

class ContentFragment : Fragment() {
    private lateinit var binding: FragmentContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater)
//        container?.addView(binding.root)
        return binding.root
//        val view = createContentView()
//        container?.let { container.addView(view) }
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createContentView(): View {
        val view = View(requireContext())
        view.setBackgroundColor(Color.DKGRAY)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setBackgroundColor(Color.DKGRAY)
        return view
    }
}