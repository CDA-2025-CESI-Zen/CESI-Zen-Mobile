package fr.cesizen.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config : KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            infrastructureModule,
            platformInfrastructureModule,
            presentationModule,
            platformPresentationModule
        )
    }
}