package sg.entvision.tmssystem.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import sg.entvision.tmssystem.persistance.*

@Database(
    entities = [LocationTable::class, DriverTable::class, RecipientTable::class, ShipperTable::class, JobTable::class,DetailTable::class],
    version = 4,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun driverDao(): DriverInfomationDao
    abstract fun shipperDao(): ShipperInformationDao
    abstract fun recipientDao(): RecipientInformationDao
    abstract fun deliveryDao(): DeliveryDao
}