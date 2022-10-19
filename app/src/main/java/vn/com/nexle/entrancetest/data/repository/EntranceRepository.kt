package vn.com.nexle.entrancetest.data.repository

import kotlinx.coroutines.flow.Flow
import vn.com.nexle.entrancetest.data.remote.model.SignInItem

interface EntranceRepository {
    suspend fun signIn(email: String, password: String, remember: Boolean) : Flow<Result<SignInItem>>
    suspend fun signUp(firstName: String, lastName: String, email: String, password: String) : Flow<Result<SignInItem>>
    suspend fun logOut() : Flow<Result<Boolean>>
}