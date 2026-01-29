package fr.cesizen.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

actual val platformInfrastructureModule = module {

    single<HttpClientEngine> {
        OkHttp.create {
            preconfigured = unsafeOkHttpClient()
        }
    }
}

private fun unsafeOkHttpClient(): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) = Unit

            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }
    )

    val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    val sslSocketFactory = sslContext.socketFactory
    val trustManager = trustAllCerts[0] as X509TrustManager

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustManager)
        .hostnameVerifier { _, _ -> true }
        .build()
}
