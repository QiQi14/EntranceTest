package vn.com.nexle.entrancetest.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vn.com.nexle.entrancetest.ConstrantTestItem.signInItem
import vn.com.nexle.entrancetest.data.remote.model.SignInItem
import vn.com.nexle.entrancetest.data.repository.EntranceRepository

class FakeRepository : EntranceRepository {
    override suspend fun signIn(
        email: String,
        password: String,
        remember: Boolean
    ): Flow<Result<SignInItem>> = flow {
        emit(signInItem)
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<Result<SignInItem>>  = flow {
        emit(signInItem)
    }

    override suspend fun logOut(): Flow<Result<Boolean>> = flow {
        emit(Result.success(true))
    }
}