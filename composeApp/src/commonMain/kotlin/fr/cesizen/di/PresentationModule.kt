package fr.cesizen.di

import fr.cesizen.presentation.viewmodels.CategoriesViewModel
import fr.cesizen.presentation.viewmodels.CategoryViewModel
import fr.cesizen.presentation.viewmodels.PageViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformPresentationModule : Module
val presentationModule = module {
    viewModelOf(::CategoriesViewModel)
    viewModelOf(::CategoryViewModel)
    viewModelOf(::PageViewModel)
}