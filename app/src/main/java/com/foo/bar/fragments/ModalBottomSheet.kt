package com.foo.bar.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foo.bar.databinding.FragmentModalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(val contentView: View? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentModalBinding

    constructor() : this(null)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setContentView(contentView!!)
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
    }
    companion object {
        const val TAG = "Modal"
    }
}