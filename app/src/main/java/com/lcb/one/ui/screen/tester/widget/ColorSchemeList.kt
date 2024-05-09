package com.lcb.one.ui.screen.tester.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

fun LazyListScope.addColorSchemeList(scheme: ColorScheme) {
    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.primary, "primary")
            ColorSurface(Modifier.weight(1f), scheme.onPrimary, "onPrimary")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.primaryContainer, "primaryContainer")
            ColorSurface(Modifier.weight(1f), scheme.onPrimaryContainer, "onPrimaryContainer")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.secondary, "secondary")
            ColorSurface(Modifier.weight(1f), scheme.onSecondary, "onSecondary")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.secondaryContainer, "secondaryContainer")
            ColorSurface(Modifier.weight(1f), scheme.onSecondaryContainer, "onSecondaryContainer")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.tertiary, "tertiary")
            ColorSurface(Modifier.weight(1f), scheme.onTertiary, "onTertiary")
        }
    }


    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.tertiaryContainer, "tertiaryContainer")
            ColorSurface(Modifier.weight(1f), scheme.onTertiaryContainer, "onTertiaryContainer")
        }
    }


    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.error, "error")
            ColorSurface(Modifier.weight(1f), scheme.onError, "onError")
        }
    }


    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.errorContainer, "errorContainer")
            ColorSurface(Modifier.weight(1f), scheme.onErrorContainer, "onErrorContainer")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.background, "background")
            ColorSurface(Modifier.weight(1f), scheme.onBackground, "onBackground")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.surface, "surface")
            ColorSurface(Modifier.weight(1f), scheme.onSurface, "onSurface")
        }
    }


    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.surfaceVariant, "surfaceVariant")
            ColorSurface(Modifier.weight(1f), scheme.onSurfaceVariant, "onSurfaceVariant")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.inverseSurface, "inverseSurface")
            ColorSurface(Modifier.weight(1f), scheme.inverseOnSurface, "inverseOnSurface")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.inversePrimary, "inversePrimary")
            ColorSurface(Modifier.weight(1f), scheme.scrim, "scrim")
        }
    }


    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.outline, "outline")
            ColorSurface(Modifier.weight(1f), scheme.outlineVariant, "outlineVariant")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.surfaceBright, "surfaceBright")
            ColorSurface(Modifier.weight(1f), scheme.surfaceDim, "surfaceDim")
        }
    }

    item {
        Row {
            ColorSurface(
                Modifier.weight(1f),
                scheme.surfaceContainerLowest,
                "surfaceContainerLowest"
            )
            ColorSurface(Modifier.weight(1f), scheme.surfaceContainerLow, "surfaceContainerLow")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.surfaceContainerHigh, "surfaceContainerHigh")
            ColorSurface(Modifier.weight(1f), scheme.surfaceContainerHighest, "surfaceContainerHighest")
        }
    }

    item {
        Row {
            ColorSurface(Modifier.weight(1f), scheme.surfaceContainer, "surfaceContainer")
            ColorSurface(Modifier.weight(1f), scheme.surfaceTint, "surfaceTint")
        }
    }
}

@Composable
private fun ColorSurface(modifier: Modifier = Modifier, color: Color, label: String) {
    Surface(
        color = color,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "${label}\n${color.toHex()}",
            style = MaterialTheme.typography.labelMedium,
            color = color.textColor()
        )
    }
}

fun Color.textColor(): Color {
    return if (convert(ColorSpaces.Srgb).luminance() > 0.5) Color.Black else Color.White
}

fun Color.toHex(): String {
    val alpha = (alpha * 255).toInt()
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return "#%02X%02X%02X%02X".format(alpha, red, green, blue)
}
