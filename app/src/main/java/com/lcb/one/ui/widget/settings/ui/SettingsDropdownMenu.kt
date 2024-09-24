package com.lcb.one.ui.widget.settings.ui

import androidx.annotation.IntRange
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lcb.one.R

@Composable
fun SettingsDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0)
    selectIndex: Int = 0,
    title: String,
    options: Array<String>,
    icon: (@Composable () -> Unit)? = null,
    colors: ListItemColors = SettingsDefaults.colorsDefault(),
    onItemSelected: ((Int) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    SimpleSettingsMenuLink(
        colors = colors,
        modifier = modifier.minSettingsHeight(),
        enabled = enabled,
        icon = icon,
        title = title,
        summary = options[selectIndex],
        action = {
            Icon(painterResource(R.drawable.ic_unfold_more), null)
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        trailingIcon = {
                            if (index == selectIndex) {
                                Icon(Icons.Rounded.Check, null)
                            }
                        },
                        text = { Text(text = option) },
                        onClick = {
                            onItemSelected?.invoke(index)
                            expanded = false
                        }
                    )
                }
            }
        },
        onClick = { expanded = true }
    )
}