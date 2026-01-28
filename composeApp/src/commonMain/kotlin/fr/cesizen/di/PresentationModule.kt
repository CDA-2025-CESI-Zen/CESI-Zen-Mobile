package fr.cesizen.di

import fr.cesizen.presentation.services.ToastService
import fr.cesizen.presentation.viewmodels.CategoriesViewModel
import fr.cesizen.presentation.viewmodels.CategoryViewModel
import fr.cesizen.presentation.viewmodels.DiagnosisViewModel
import fr.cesizen.presentation.viewmodels.ForgottenPasswordViewModel
import fr.cesizen.presentation.viewmodels.PageViewModel
import fr.cesizen.presentation.viewmodels.ProfileViewModel
import fr.cesizen.presentation.viewmodels.SignInViewModel
import fr.cesizen.presentation.viewmodels.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformPresentationModule : Module
val presentationModule = module {

    single { ToastService() }

    viewModelOf(::CategoriesViewModel)
    viewModelOf(::CategoryViewModel)
    viewModelOf(::PageViewModel)
    viewModelOf(::DiagnosisViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::ForgottenPasswordViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ProfileViewModel)
}