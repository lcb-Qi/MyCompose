package com.lcb.one.ui.screen.main.more

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.R
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.screen.about.AboutScreen
import com.lcb.one.ui.screen.settings.SettingsScreen
import com.lcb.one.ui.widget.settings.ui.ProvideSettingsItemColor
import com.lcb.one.ui.widget.settings.ui.SettingsDefaults
import com.lcb.one.ui.widget.settings.ui.SimpleSettingsMenuLink

@Composable
fun MoreScreen(navController: NavHostController) {
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        ProvideSettingsItemColor(SettingsDefaults.colorOnCard()) {
            // 设置
            SimpleSettingsMenuLink(
                modifier = Modifier.padding(top = 8.dp),
                title = stringResource(R.string.settings),
                icon = { Icon(Icons.Rounded.Settings, null) },
                onClick = { navController.launchSingleTop(SettingsScreen) }
            )

            // 关于
            SimpleSettingsMenuLink(
                modifier = Modifier.padding(bottom = 8.dp),
                title = stringResource(R.string.about),
                icon = { Icon(Icons.Rounded.Info, null) },
                onClick = { navController.launchSingleTop(AboutScreen) }
            )
        }
    }
}
