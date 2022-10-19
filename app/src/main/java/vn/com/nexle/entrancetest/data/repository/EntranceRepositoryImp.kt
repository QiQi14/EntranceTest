package vn.com.nexle.entrancetest.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import vn.com.nexle.entrancetest.data.local.source.PreferenceSource
import vn.com.nexle.entrancetest.data.remote.model.SignInItem
import vn.com.nexle.entrancetest.data.remote.source.EntranceRemoteDataSource
import javax.inject.Inject

class EntranceRepositoryImp @Inject constructor(
    private val remoteDataSource: EntranceRemoteDataSource,
    private val localDataSource: PreferenceSource
) : EntranceRepository {
    companion object {
        private const val PARAMS_FIRST_NAME = "firstName"
        private const val PARAMS_LAST_NAME = "lastName"
        private const val PARAMS_EMAIL = "email"
        private const val PARAMS_PASSWORD = "password"
        private const val PARAMS_AUTHORIZATION = "Authorization"
    }

    override suspend fun signIn(email: String, password: String, remember: Boolean): Flow<Result<SignInItem>> = flow {
        val body = hashMapOf(
            PARAMS_EMAIL to email,
            PARAMS_PASSWORD to password
        )

        val response = remoteDataSource.signIn(body)

        localDataSource.saveAccessToken(response.token)
        localDataSource.saveRefreshToken(response.refreshToken)
        localDataSource.saveDisplayName(response.displayName)

        emit(Result.success(response))
    }.catch { ex ->
        emit(Result.failure(ex))
    }.flowOn(Dispatchers.IO)

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<Result<SignInItem>> = flow {
        val body = hashMapOf(
            PARAMS_FIRST_NAME to firstName,
            PARAMS_LAST_NAME to lastName,
            PARAMS_EMAIL to email,
            PARAMS_PASSWORD to password
        )

        val response = remoteDataSource.signUp(body)

        localDataSource.saveAccessToken(response.token)
        localDataSource.saveRefreshToken(response.refreshToken)
        localDataSource.saveDisplayName(response.displayName)

        emit(Result.success(response))
    }.catch { ex ->
        emit(Result.failure(ex))
    }.flowOn(Dispatchers.IO)

    override suspend fun logOut(): Flow<Result<Boolean>> = flow {
        val response = remoteDataSource.logOut(hashMapOf(PARAMS_AUTHORIZATION to localDataSource.getAccessToken()))
        Log.e("logout", response)

        localDataSource.removeAccessToken()
        localDataSource.removeRefreshToken()
        localDataSource.removeDisplayName()
        emit(Result.success(true))
    }.catch { ex ->
        localDataSource.removeAccessToken()
        localDataSource.removeRefreshToken()
        localDataSource.removeDisplayName()
        emit(Result.failure(ex))
    }.flowOn(Dispatchers.IO)
}