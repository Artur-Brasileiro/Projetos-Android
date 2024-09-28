package br.com.projetomanaconfeccoes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FixedColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    background = White,
    onBackground = Black
)

@Composable
fun ProjetoManaConfeccoesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FixedColorScheme,
        typography = Typography,
        content = content
    )
}