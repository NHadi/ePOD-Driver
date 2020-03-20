package sg.entvision.tmssystem.di

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import sg.entvision.tmssystem.persistance.*
import sg.entvision.tmssystem.repository.Database
import sg.entvision.tmssystem.repository.LocationRepository
import sg.entvision.tmssystem.repository.RecipientTable
import sg.entvision.tmssystem.utility.workmanager.TrackLocationWorker

val localDataSourceModule = module {
    single(createdAtStart = true) { provideDatabase(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideDeliveryDao(get()) }
    single { provideDriverDao(get()) }
    single { provideRecipientDao(get()) }
    single { provideShipperDao(get()) }
    single { LocationRepository(androidApplication(), get(), get()) }
    /**
     * another repository here
     */
}


fun provideDatabase(application: Application): Database {
    return Room
        .databaseBuilder(application, Database::class.java, "TMSAPP.db")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideLocationDao(database: Database): LocationDao {
    return database.locationDao()
}

fun provideDriverDao(database: Database): DriverInfomationDao {
    return database.driverDao()
}

fun provideShipperDao(database: Database): ShipperInformationDao {
    return database.shipperDao()
}

fun provideRecipientDao(database: Database): RecipientInformationDao {
    return database.recipientDao()
}

fun provideDeliveryDao(database: Database): DeliveryDao {
    return database.deliveryDao()
}




