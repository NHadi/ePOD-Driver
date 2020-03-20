package sg.entvision.tmssystem.model.responses

data class DailyRoute(
    val `data`: DataRoute,
    val msg: String,
    val status: String
)

data class DataRoute(
    val destination: Destination,
    val driverID: Int,
    val origin: Origin,
    val polyline: String,
    val scheduleDate: String,
    val vehicleCode: String,
    val waypoints: List<Waypoint>
)

data class Destination(
    val latitude: String,
    val longitude: String
)

data class Origin(
    val latitude: String,
    val longitude: String
)

data class Waypoint(
    val latitude: String,
    val longitude: String,
    val type: String,
    val waypointID: String
)