package vn.com.nexle.entrancetest.data.remote.source

import vn.com.nexle.entrancetest.data.remote.model.SignInItem
import vn.com.nexle.entrancetest.data.remote.service.EntranceService
import javax.inject.Inject

class EntranceRemoteDataSourceImp @Inject constructor(
    private val restfulService: EntranceService,
): EntranceRemoteDataSource{

    override suspend fun signIn(body: HashMap<String, String>): SignInItem {
        return restfulService.signIn(body = body)
    }

    override suspend fun signUp(body: HashMap<String, String>): SignInItem {
        return restfulService.signUp(body = body)
    }

    override suspend fun logOut(header: HashMap<String, String>) : String {
        return restfulService.logOut(header).string()
    }
}