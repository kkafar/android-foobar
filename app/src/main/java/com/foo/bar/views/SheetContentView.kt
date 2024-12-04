package com.foo.bar.views

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.foo.bar.FragmentDismissalDelegate
import com.foo.bar.ext.ColorUtils

class SheetContentView(context: Context, private val tileHeight: Int = 200, private val tileCount: Int = 5, private val dismissalDelegate: FragmentDismissalDelegate? = null) : LinearLayout(context) {
    private val spacingBottom = 50

    init {
        orientation = VERTICAL
        createMosaicContent(context)
        createDismissButton(context)
    }

    private fun createMosaicContent(context: Context) {
        repeat(tileCount) {
            addView(createMosaicTile(context))
        }
    }

    private fun createMosaicTile(context: Context): View {
        return View(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                tileHeight
            ).apply {
                bottomMargin = spacingBottom
            }
            setBackgroundColor(ColorUtils.randomColor())
            elevation = 24f
        }
    }

    private fun createDismissButton(context: Context) {
        val button = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = "Dismiss sheet"
            this.elevation = 48f
            setOnClickListener {
                dismissalDelegate?.dismissFragment()
            }
        }

        addView(button)
    }
}