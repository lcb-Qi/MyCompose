package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lcb.one.ui.widget.settings.ui.internal.SettingsTileScaffold

@Composable
fun SettingsMenuLink(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: (@Composable () -> Unit)? = null,
  title: @Composable () -> Unit,
  subtitle: (@Composable () -> Unit)? = null,
  action: (@Composable (Boolean) -> Unit)? = null,
  onClick: () -> Unit,
) {
  Surface {
    Row(
      modifier = modifier.fillMaxWidth()
        .clickable(
          enabled = enabled,
          onClick = onClick,
        ),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      SettingsTileScaffold(
        title = title,
        enabled = enabled,
        summary = subtitle,
        icon = icon,
        action = action,
        actionDivider = true,
      )
    }
  }
}
