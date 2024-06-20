package com.lcb.one.ui.screen.applist.widget

import com.lcb.one.localization.Localization

enum class AppType {
    USER, SYSTEM, ALL;

    fun toDisplayName() = when (this) {
        USER -> Localization.userApps
        SYSTEM -> Localization.systemApps
        ALL -> Localization.allApps
    }
}

