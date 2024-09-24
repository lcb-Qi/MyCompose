package com.lcb.one.util.platform

import android.content.res.Resources
import androidx.annotation.StringRes
import com.lcb.one.ui.MyApp

object Res {
    private fun getRes(): Resources = MyApp.get().resources

    fun string(@StringRes resId: Int): String {
        return getRes().getString(resId)
    }

    fun string(@StringRes resId: Int, vararg args: Any): String {
        return getRes().getString(resId, *args)
    }
}