package sg.entvision.tmssystem.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Location information lat n long
 */
@Entity
data class LocationTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id",index = true) val id: Long,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)

/**
 * Driver Infomation
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocationTable::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"]
        )
    ]
)
data class DriverTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "driver_id") val id: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "location_id") val locationID: Long,
    @ColumnInfo(name = "photoProfile") val photoProfile: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)


/**
 * List of delivery
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DriverTable::class,
            parentColumns = ["driver_id"],
            childColumns = ["driver_id"]
        ),
        ForeignKey(
            entity = RecipientTable::class,
            parentColumns = ["recipient_id"],
            childColumns = ["recipient_id"]
        ),
        ForeignKey(
            entity = ShipperTable::class,
            parentColumns = ["shipper_id"],
            childColumns = ["shipper_id"]
        ),
        ForeignKey(
            entity = DetailTable::class,
            parentColumns = ["detail_id"],
            childColumns = ["detail_id"]
        )
    ]
)
data class JobTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "job_id",index = true) val id: Long,
    @ColumnInfo(name = "shipper_id") val senderID: Long,
    @ColumnInfo(name = "driver_id") val driverID: Long,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "jobStatus") val jobStatus: String,
    @ColumnInfo(name = "recipient_id") val recipientID: Long,
    @ColumnInfo(name = "detail_id") val detailID: Long,
    @ColumnInfo(name = "vehicle_code") val vehicleCode : String,
    @ColumnInfo(name = "preferredPickupTime") val preferredPickupTime : String,
    @ColumnInfo(name = "preferredDeliveryTime") val preferredDeliveryTime : String,
    @ColumnInfo(name = "scheduledDeliveryTime") val scheduledDeliveryTime : String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocationTable::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"]
        )
    ]
)
data class RecipientTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "recipient_id",index = true) val id: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "photoProfile") val photoProfile: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "deliveryAddress") val deliveryAddress : String,
    @ColumnInfo(name = "deliveryTel") val deliveryTel : String,
    @ColumnInfo(name = "location_id") val locationID: Long
    )


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocationTable::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"]
        )
    ]
)
data class ShipperTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "shipper_id",index = true) val id: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "photoProfile") val photoProfile: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "location_id") val locationID: Long,
    @ColumnInfo(name = "pickupAddress") val pickupAddress : String,
    @ColumnInfo(name = "pickupTel") val pickupTel: String
    )


@Entity
data class DetailTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "detail_id",index = true) val id: Long,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "description") val description: String
)

