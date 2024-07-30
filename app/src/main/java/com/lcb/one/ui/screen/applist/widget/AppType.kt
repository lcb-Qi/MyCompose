package com.lcb.one.ui.screen.applist.widget

import com.lcb.one.R
import com.lcb.one.util.android.Res

enum class AppType {
    USER, SYSTEM, ALL;

    fun toDisplayName() = when (this) {
        USER -> Res.string(R.string.user_apps)
        SYSTEM -> Res.string(R.string.system_apps)
        ALL -> Res.string(R.string.all_apps)
    }
}

