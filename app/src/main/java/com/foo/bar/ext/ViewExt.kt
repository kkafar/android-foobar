package com.foo.bar.ext

import android.view.View
import android.view.ViewGroup

internal fun View.parentAsView() = this.parent as? View

internal fun View.parentAsViewGroup() = this.parent as? ViewGroup
