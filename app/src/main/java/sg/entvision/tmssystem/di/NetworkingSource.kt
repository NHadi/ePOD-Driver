package sg.entvision.tmssystem.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import sg.entvision.tmssystem.BuildConfig
import sg.entvision.tmssystem.TmsApp
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.network.ApiService
import sg.entvision.tmssystem.network.MapService
import sg.entvision.tmssystem.network.TLSFactory
import sg.entvision.tmssystem.utility.PreferencesHelper
import timber.log.Timber
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


var apiBaseUrl: String = "https://jsonplaceholder.typicode.com"
var apiMapUrl: String = "https://maps.googleapis.com/maps/"
val remoteDataSourceModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit<ApiService>(get()) }
    single { provideMapApi<MapService>(get()) }
}

private fun getLogInterceptor() = HttpLoggingInterceptor().apply {
    level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}

/**
 * TLS certificate
 * @return X509TrustManager?
 */
private fun getTrustManager(): X509TrustManager? {
    var trustManagerFactory: TrustManagerFactory? = null
    return try {
        trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory!!.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            null
        } else trustManagers[0] as X509TrustManager
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        null
    } catch (e: KeyStoreException) {
        e.printStackTrace()
        null
    }

}


fun provideOkHttpClient(): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
    try {
        var trustManager = getTrustManager()
        if (trustManager != null) {
            var socketFactory = TLSFactory()
            okHttpClient.sslSocketFactory(socketFactory, trustManager)
        }
    } catch (e: KeyManagementException) {
        Timber.e(e.printStackTrace().toString())
        e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
        Timber.e(e.printStackTrace().toString())
        e.printStackTrace()
    }

    okHttpClient.hostnameVerifier { s, sslSession ->
        true
    }
    okHttpClient.connectTimeout(Consts.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
    okHttpClient.readTimeout(Consts.READ_TIMEOUT, TimeUnit.MILLISECONDS)
    okHttpClient.writeTimeout(Consts.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
    okHttpClient.addInterceptor(getLogInterceptor())
    okHttpClient.addInterceptor(
        object : Interceptor {
            val UNAUTHORIZED = 401
            val MAINTENANCE = 503
            override fun intercept(chain: Interceptor.Chain): Response {
                var context = TmsApp.getAppContext()
                var response = chain.proceed(chain.request())
                if (response.code() == UNAUTHORIZED) {
                    PreferencesHelper.clearAllSharedData(context)
//                    ContextCompat.startActivity(
//                        context,
//                        Intent(TmsApp.getAppContext(), ActivitySplash::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                        , null
//                    )
                    return response
                } else if (MAINTENANCE == response.code()) {

                }
                return response
            }
        }
    )
    return okHttpClient.build()
}


inline fun <reified T> provideRetrofit(okhttpclient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(apiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okhttpclient)
        .build()
    return retrofit.create()
}

inline fun <reified T> provideMapApi(okhttpclient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(apiMapUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okhttpclient)
        .build()
    return retrofit.create()
}