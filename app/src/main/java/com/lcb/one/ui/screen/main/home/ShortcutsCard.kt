package com.lcb.one.ui.screen.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.prefs.UserPrefs
import com.lcb.one.ui.Route
import com.lcb.one.ui.Screen
import com.lcb.weight.AppTextButton
import kotlinx.coroutines.launch

private val userShortcuts by lazy {
    val set = UserPrefs.getBlocking(UserPrefs.Key.shortcuts, emptySet())
    val shortcuts = Route.supportShortcutScreens.filter { set.contains(it.route) }

    shortcuts.toMutableStateList()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShortcutsCard(onShortcutClick: (String) -> Unit) {
    val scope = rememberCoroutineScope()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.shortcut),
                style = MaterialTheme.typography.titleMedium
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                userShortcuts.forEach {
                    ElevatedAssistChip(
                        onClick = { onShortcutClick(it.route) },
                        label = { Text(text = it.label) }
                    )
                }

                var showAdd by remember { mutableStateOf(false) }
                SuggestionChip(
                    onClick = { showAdd = true },
                    label = { Text(text = stringResource(R.string.add_shortcut)) },
                    icon = { Icon(Icons.Rounded.AddCircle, null) }
                )

                AddShortcutsDialog(
                    show = showAdd,
                    onDismiss = { showAdd = false },
                    onSelectFinish = {
                        userShortcuts.clear()
                        userShortcuts.addAll(it)
                        val routes = it.map { it.route }.toSet()
                        scope.launch { UserPrefs.put(UserPrefs.Key.shortcuts, routes) }
                    }
                )
            }
        }
    }
}

@Composable
private fun AddShortcutsDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSelectFinish: (List<Screen>) -> Unit = {}
) {
    if (!show) return

    val supportShortcutScreens = Route.supportShortcutScreens.toList()
    val selectedScreen = remember { mutableStateListOf<Screen>() }
    LaunchedEffect(Unit) {
        selectedScreen.addAll(userShortcuts)
    }

    val dialogContent: @Composable () -> Unit = {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(count = supportShortcutScreens.size, key = { it }) { index ->
                    val screen = supportShortcutScreens[index]
                    val selected = screen in selectedScreen
                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (selected) {
                                selectedScreen.remove(screen)
                            } else {
                                selectedScreen.add(screen)
                            }
                        },
                        label = { Text(text = screen.label) },
                        trailingIcon = {
                            Icon(
                                if (selected) Icons.Rounded.CheckBox else Icons.Rounded.CheckBoxOutlineBlank,
                                null
                            )
                        }
                    )
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_shortcut)) },
        dismissButton = {
            AppTextButton(text = stringResource(R.string.cancel), onClick = onDismiss)
        },
        confirmButton = {
            AppTextButton(
                text = stringResource(R.string.ok),
                onClick = {
                    onDismiss()
                    onSelectFinish(selectedScreen)
                }
            )
        },
        text = dialogContent
    )
}
