package com.foo.bar.ext

import android.view.View

internal fun View.parentView(): View? {
    return this.parent as? View
}
