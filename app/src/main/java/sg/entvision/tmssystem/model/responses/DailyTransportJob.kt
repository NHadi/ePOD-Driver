package sg.entvision.tmssystem.model.responses

data class DailyTransportJob(
    val `data`: List<Data>,
    val msg: String,
    val status: String
)

data class Data(
    val driverID: String,
    val jobs: List<Job>,
    val transportDate: String,
    val vehicleCode: String
)

data class Job(
    val ExtendedInfo: String,
    val deliveryAddress: String,
    val deliveryContact: String,
    val deliveryTel: String,
    val deliveryWaypointID: String,
    val jobNo: String,
    val jobSeq: String,
    val jobStatus: String,
    val pickupAddress: String,
    val pickupContact: String,
    val pickupTel: String,
    val pickupWaypointID: String,
    val preferredDeliveryTime: String,
    val preferredPickupTime: String,
    val scheduledDeliveryTime: String,
    val transportPacking: List<TransportPacking>
)

data class TransportPacking(
    val itemDescription: String,
    val quantity: Int,
    val size: Size,
    val unit: String,
    val weight: String
)

data class Size(
    val height: String,
    val length: String,
    val width: String
)