package vn.com.nexle.entrancetest.data.remote.service

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import vn.com.nexle.entrancetest.data.remote.model.SignInItem

interface EntranceService {
    @POST("api/auth/signin")
    suspend fun signIn(@Body body: HashMap<String, String>) : SignInItem

    @POST("api/auth/signup")
    suspend fun signUp(@Body body: HashMap<String, String>) : SignInItem

    @POST("api/auth/logout")
    suspend fun logOut(@HeaderMap header: HashMap<String, String>) : ResponseBody
}