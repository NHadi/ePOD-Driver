package sg.entvision.tmssystem.network

import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("/posts/1/")
    fun hitAPI(): Maybe<Response<ResponseBody>>
}