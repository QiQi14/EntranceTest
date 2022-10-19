package vn.com.nexle.entrancetest.data.local.source

interface PreferenceSource {
    fun saveAccessToken(token: String)
    fun getAccessToken(): String
    fun removeAccessToken()

    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String
    fun removeRefreshToken()

    fun saveDisplayName(name: String)
    fun getDisplayName(): String
    fun removeDisplayName()
}