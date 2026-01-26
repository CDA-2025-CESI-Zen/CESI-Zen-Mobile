package fr.cesizen

import androidx.compose.ui.window.ComposeUIViewController
import fr.cesizen.di.initKoin
import fr.cesizen.presentation.App

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }