package com.lcb.one.ui.screen.zxing

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.lcb.one.ui.Screen

object QrCodeScreen : Screen {
    override val route: String
        get() = "QrCode"

    @Composable
    override fun Content(navController: NavHostController, args: Bundle?) {
    }
}
