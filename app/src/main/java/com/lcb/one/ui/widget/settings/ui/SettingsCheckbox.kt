package com.lcb.one.ui.widget.settings.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.lcb.one.ui.widget.settings.storage.SettingValueState
import com.lcb.one.ui.widget.settings.storage.getValue
import com.lcb.one.ui.widget.settings.storage.setValue
import com.lcb.one.ui.widget.settings.ui.internal.SettingsTileScaffold

@Composable
fun SettingsCheckbox(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  state: SettingValueState<Boolean>,
  icon: @Composable (() -> Unit)? = null,
  title: @Composable () -> Unit,
  subtitle: @Composable (() -> Unit)? = null,
  checkboxColors: CheckboxColors = CheckboxDefaults.colors(),
  onCheckedChange: (Boolean) -> Unit = {},
) {
  var storageValue by state
  val update: (Boolean) -> Unit = { boolean ->
    storageValue = boolean
    onCheckedChange(storageValue)
  }
  Surface {
    Row(
      modifier = modifier
        .fillMaxWidth()
        .toggleable(
          enabled = enabled,
          value = storageValue,
          role = Role.Checkbox,
          onValueChange = { update(!storageValue) },
        ),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      SettingsTileScaffold(
        enabled = enabled,
        title = title,
        summary = subtitle,
        icon = icon,
        action = {
          Checkbox(
            enabled = enabled,
            checked = storageValue,
            onCheckedChange = update,
            colors = checkboxColors,
          )
        },
      )
    }
  }
}
