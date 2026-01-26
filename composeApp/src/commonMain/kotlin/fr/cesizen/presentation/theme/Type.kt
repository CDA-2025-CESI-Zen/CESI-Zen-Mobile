package fr.cesizen.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import cesizen.composeapp.generated.resources.Res
import cesizen.composeapp.generated.resources.marianne_bold
import cesizen.composeapp.generated.resources.marianne_extra_bold
import cesizen.composeapp.generated.resources.marianne_regular
import org.jetbrains.compose.resources.Font

val AppTypography
    @Composable
    get() = Typography().let {
        val regular   = FontFamily(Font(Res.font.marianne_regular))
        val bold      = FontFamily(Font(Res.font.marianne_bold))
        val extrabold = FontFamily(Font(Res.font.marianne_extra_bold))
        it.copy(
            displayLarge = it.displayLarge.copy(fontFamily = extrabold),
            displayMedium = it.displayMedium.copy(fontFamily = extrabold),
            displaySmall = it.displaySmall.copy(fontFamily = extrabold),
            headlineLarge = it.headlineLarge.copy(fontFamily = extrabold),
            headlineMedium = it.headlineMedium.copy(fontFamily = extrabold),
            headlineSmall = it.headlineSmall.copy(fontFamily = extrabold),
            titleLarge = it.titleLarge.copy(fontFamily = bold),
            titleMedium = it.titleMedium.copy(fontFamily = bold),
            titleSmall = it.titleSmall.copy(fontFamily = bold),
            bodyLarge = it.bodyLarge.copy(fontFamily = regular),
            bodyMedium = it.bodyMedium.copy(fontFamily = regular),
            bodySmall = it.bodySmall.copy(fontFamily = regular),
            labelLarge = it.labelLarge.copy(fontFamily = regular),
            labelMedium = it.labelMedium.copy(fontFamily = regular),
            labelSmall = it.labelSmall.copy(fontFamily = regular)
        )
    }
