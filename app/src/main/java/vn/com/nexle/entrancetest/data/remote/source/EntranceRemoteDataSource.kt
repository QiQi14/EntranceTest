package vn.com.nexle.entrancetest.data.remote.source

import vn.com.nexle.entrancetest.data.remote.model.SignInItem

interface EntranceRemoteDataSource {
    suspend fun signIn(body: HashMap<String, String>) : SignInItem
    suspend fun signUp(body: HashMap<String, String>) : SignInItem
    suspend fun logOut(header: HashMap<String, String>) : String
}