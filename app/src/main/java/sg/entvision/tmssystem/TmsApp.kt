package sg.entvision.tmssystem

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.di.localDataSourceModule
import sg.entvision.tmssystem.di.remoteDataSourceModule
import sg.entvision.tmssystem.di.viewModelModule
import sg.entvision.tmssystem.utility.HandleNotification
import sg.entvision.tmssystem.view.MainActivity
import sg.entvision.tmssystem.view.direction.DirectionRouteActivity
import timber.log.Timber

class TmsApp : Application() {

    companion object {
        private lateinit var sInstance: TmsApp
        fun getAppContext(): TmsApp {
            return sInstance
        }

        @Synchronized
        private fun setInstance(app: TmsApp) {
            sInstance = app
        }
    }

    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        initalizeComponent()
    }

    private fun initalizeComponent() {
        startKoin {
            androidLogger()
            androidContext(this@TmsApp)
            modules(listOf(viewModelModule, localDataSourceModule, remoteDataSourceModule))
        }
        //Debug Purpose
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .setNotificationOpenedHandler(NotificationHandler())
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }

    inner class NotificationHandler : OneSignal.NotificationOpenedHandler {
        override fun notificationOpened(result: OSNotificationOpenResult?) {
            result?.let {
                try {
                    val data = result.notification.payload.additionalData
                    data?.let {
                        Timber.e("$it")
                        when {
                            it.haveData() -> handleNotification(data)
                            else -> {
                                startActivity(
                                    Intent(
                                        this@TmsApp,
                                        MainActivity::class.java
                                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                )
                            }

                        }
                    }
                } catch (ex: Exception) {

                }
            }
        }
    }


    private fun JSONObject.haveData(): Boolean {
        return this.length() != 0
    }

    fun handleNotification(data: JSONObject) {
        var datas = object : HandleNotification(data) {
            override fun openMain() {
                startActivity(
                    Intent(
                        this@TmsApp,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
            }

            override fun openTracking(lat: Double, lng: Double) {
                Timber.e("$lat : $lng loh")
                startActivity(
                    Intent(
                        this@TmsApp,
                        DirectionRouteActivity::class.java
                    ).putExtra("LAT", lat).putExtra("LON", lng)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
            }
        }
    }

}