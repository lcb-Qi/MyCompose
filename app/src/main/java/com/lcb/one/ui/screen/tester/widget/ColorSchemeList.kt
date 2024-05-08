package com.lcb.one.ui.screen.tester.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.lcb.one.util.android.LLog
import kotlin.reflect.full.memberProperties

private const val TAG = "ColorSchemeList"

@Composable
fun ColorSchemeList(modifier: Modifier = Modifier, colorScheme: ColorScheme) {
    val colors = colorScheme::class.memberProperties.asSequence()
        .filter { it.returnType.toString() == Color::class.qualifiedName }
        .sortedBy { it.name }
        .toList()

    LazyColumn(modifier = modifier.background(Color.White)) {
        items(colors.size, key = { colors[it].name }) {
            val color = colors[it].call(colorScheme) as Color
            val contentColorFor = colorScheme.contentColorFor(color)
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(color = color),
                    content = {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "${colors[it].name}(${color.toHex()})",
                            color = if (contentColorFor == Color.Unspecified) Color.White else contentColorFor
                        )
                    }
                )
            }
        }
    }
}

fun Color.toHex(): String {
    val alpha = (alpha * 255).toInt()
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return "#%02X%02X%02X%02X".format(alpha, red, green, blue)
}
