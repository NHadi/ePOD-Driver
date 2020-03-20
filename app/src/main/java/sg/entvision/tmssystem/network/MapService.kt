package sg.entvision.tmssystem.network

import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import sg.entvision.tmssystem.model.responses.MapResponse

interface MapService {
    @GET("api/directions/json")
    fun getRoute(
        @Query("origin") origin: String, @Query("destination") destination: String
        , @Query("mode") mode: String, @Query("sensor") sensor: String
        , @Query("key") key: String
    ): Single<Response<MapResponse>>
}