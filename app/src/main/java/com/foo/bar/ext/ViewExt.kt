package com.foo.bar.ext

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

internal fun View.parentAsView() = this.parent as? View

internal fun View.parentAsViewGroup() = this.parent as? ViewGroup

internal fun ViewGroup.startViewTransitionRecursively(view: View) {
    Log.i("VIEW EXT", "$this starts transition on $view")
    this.startViewTransition(view)
    if (view is ViewGroup) {
        view.children.forEach { view.startViewTransitionRecursively(it) }
    }
}

internal fun ViewGroup.endViewTransitionRecursively(view: View) {
    Log.i("VIEW EXT", "$this ends transition on $view")
    this.endViewTransition(view)
    if (view is ViewGroup) {
        view.children.forEach { view.endViewTransitionRecursively(it) }
    }
}

internal fun ViewGroup.disassembleSubTreeRootedIn(view: View, detachSubtreeRoot: Boolean = true) {
    Log.i("VIEW EXT", "VISIT: $this")
    if (view is ViewGroup) {
        view.children.forEach { view.disassembleSubTreeInternal() }
    }

    if (detachSubtreeRoot) {
        Log.i("VIEW EXT", "$this removes child $view")
        this.removeView(view)
    }
}

private fun ViewGroup.disassembleSubTreeInternal() {
    Log.i("VIEW EXT", "VISIT: $this")
    this.children.forEach {
        if (it is ViewGroup) {
            it.disassembleSubTreeInternal()
        }
    }

    this.children.toList().forEach {
        Log.i("VIEW EXT", "$this removes child $it")
        this.removeView(it)
    }
}

internal fun View.removeFromParent() {
    Log.i("VIEW EXT", "$this removes itself from parent ${this.parent}")
    this.parentAsViewGroup()?.removeView(this)
}