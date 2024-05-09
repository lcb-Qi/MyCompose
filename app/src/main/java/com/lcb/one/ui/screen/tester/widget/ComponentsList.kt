package com.lcb.one.ui.screen.tester.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.addComponentsList() {
    item { Buttons() }
    item { SelectButtons() }
    item { Cards() }
    item { Chips() }
    item { ProgressIndicators() }
    item { TextInputs() }
}

@Composable
private fun Cards(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            ElevatedCard(onClick = {}) {
                Text(text = "Elevated Card", modifier = Modifier.padding(12.dp))
            }
        }
        item {
            OutlinedCard(onClick = {}) {
                Text(text = "Outlined Card", modifier = Modifier.padding(12.dp))
            }
        }
        item {
            Card(onClick = {}) {
                Text(text = "Card", modifier = Modifier.padding(12.dp))
            }
        }
    }
}

@Composable
private fun Buttons(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Button(onClick = {}) { Text(text = "Button") }
        }

        item {
            TextButton(onClick = {}) { Text(text = "Text Button") }
        }

        item {
            FloatingActionButton(onClick = {}) { Text(text = "FAB") }
        }

        item {
            OutlinedButton(onClick = {}) { Text(text = "Outlined Button") }
        }

        item {
            FilledTonalButton(onClick = {}) { Text(text = "Filled Tonal Button") }
        }

        item {
            ElevatedButton(onClick = {}) { Text(text = "Elevated Button") }
        }
    }
}

@Composable
private fun SelectButtons(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            var selected by remember { mutableStateOf(false) }
            RadioButton(selected = selected, onClick = { selected = !selected })
        }

        item {
            var checked by remember { mutableStateOf(false) }
            Checkbox(checked = checked, onCheckedChange = { checked = !checked })
        }

        item {
            var checked by remember { mutableStateOf(false) }
            Switch(checked = checked, onCheckedChange = { checked = !checked })
        }
    }
}

@Composable
private fun Chips(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            AssistChip(onClick = {}, label = { Text(text = "Assist Chip") })
        }

        item {
            ElevatedAssistChip(onClick = {}, label = { Text(text = "Elevated Assist Chip") })
        }
    }
}

@Composable
private fun ProgressIndicators(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { LinearProgressIndicator() }
        item { CircularProgressIndicator() }
    }
}

@Composable
private fun TextInputs(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("xxx") }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { OutlinedTextField(value = input, onValueChange = { input = it }) }
        item { TextField(value = input, onValueChange = { input = it }) }
    }
}