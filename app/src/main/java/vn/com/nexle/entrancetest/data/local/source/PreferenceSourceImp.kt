package vn.com.nexle.entrancetest.data.local.source

import vn.com.nexle.entrancetest.data.local.service.PreferencesManager
import javax.inject.Inject

class PreferenceSourceImp @Inject constructor(
    private val sharedPreferences: PreferencesManager
) : PreferenceSource {
    override fun saveAccessToken(token: String) {
        sharedPreferences.saveAccessToken(token = token)
    }

    override fun getAccessToken(): String {
        return sharedPreferences.getAccessToken()
    }

    override fun removeAccessToken() {
        sharedPreferences.removeAccessToken()
    }

    override fun saveRefreshToken(token: String) {
        sharedPreferences.saveRefreshToken(token = token)
    }

    override fun getRefreshToken(): String {
        return sharedPreferences.getRefreshToken()
    }

    override fun removeRefreshToken() {
        sharedPreferences.removeRefreshToken()
    }

    override fun saveDisplayName(name: String) {
        sharedPreferences.saveDisplayName(name = name)
    }

    override fun getDisplayName(): String {
        return sharedPreferences.getDisplayName()
    }

    override fun removeDisplayName() {
        sharedPreferences.removeDisplayName()
    }
}