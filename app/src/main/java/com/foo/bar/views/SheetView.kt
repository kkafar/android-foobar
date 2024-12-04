package com.foo.bar.views

import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.foo.bar.FragmentDismissalDelegate
import com.google.android.material.bottomsheet.BottomSheetBehavior


class SheetView(context: Context, dismissalDelegate: FragmentDismissalDelegate? = null) : FrameLayout(context) {
    val castedLayoutParams: CoordinatorLayout.LayoutParams
        get() = layoutParams as CoordinatorLayout.LayoutParams

    val sheetBehavior: BottomSheetBehavior<SheetView>
        get() = castedLayoutParams.behavior as BottomSheetBehavior<SheetView>

    val contentView: SheetContentView = SheetContentView(context, tileCount = 3, dismissalDelegate = dismissalDelegate).apply {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    init {
        setBackgroundColor(Color.WHITE)
        addView(contentView)
    }
}