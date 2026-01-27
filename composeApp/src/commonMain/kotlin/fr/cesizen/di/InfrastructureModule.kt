package fr.cesizen.di

import fr.cesizen.infrastructure.services.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformInfrastructureModule : Module
val infrastructureModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient         = true
        }
    }

    single {
        HttpClient(get()) {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single {
        ApiService(
            client = get(),
        )
    }
}
