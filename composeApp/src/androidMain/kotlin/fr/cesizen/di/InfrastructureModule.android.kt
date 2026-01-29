package fr.cesizen.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformInfrastructureModule = module {

    single<HttpClientEngine> {
        OkHttp.create { }
    }
}
