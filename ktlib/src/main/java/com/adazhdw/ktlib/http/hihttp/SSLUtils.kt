package com.adazhdw.ktlib.http.hihttp

import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*

object SSLUtils {

    fun initSSLSocketFactory(): Pair<SSLSocketFactory, X509TrustManager> {
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalArgumentException("Unexpected default trust managers:$trustManagers")
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        return sslSocketFactory to trustManager

    }
}