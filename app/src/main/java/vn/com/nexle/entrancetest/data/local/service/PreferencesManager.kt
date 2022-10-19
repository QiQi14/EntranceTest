package vn.com.nexle.entrancetest.data.local.service

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesManager @Inject constructor(
    context: Context
) {
    private var prefs: SharedPreferences
    private var helper: SharedPreferencesHelper

    init {
        val name = context.packageName.toString() + ".userprefs"
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        helper = SharedPreferencesHelper(context)
    }

    companion object {
        private const val PREF_ACCESS_TOKEN = "accessToken"
        private const val PREF_REFRESH_TOKEN = "refreshToken"
        private const val PREF_DISPLAY_NAME = "displayName"
    }

    fun saveAccessToken(token: String) {
        helper.save(PREF_ACCESS_TOKEN, token)
    }

    fun getAccessToken(): String {
        return helper.loadString(PREF_ACCESS_TOKEN, "")
    }

    fun removeAccessToken() {
        helper.remove(PREF_ACCESS_TOKEN)
    }

    fun saveRefreshToken(token: String) {
        helper.save(PREF_REFRESH_TOKEN, token)
    }

    fun getRefreshToken(): String {
        return helper.loadString(PREF_REFRESH_TOKEN, "")
    }

    fun removeRefreshToken() {
        helper.remove(PREF_REFRESH_TOKEN)
    }

    fun saveDisplayName(name: String) {
        helper.save(PREF_DISPLAY_NAME, name)
    }

    fun getDisplayName(): String {
        return helper.loadString(PREF_DISPLAY_NAME, "")
    }

    fun removeDisplayName() {
        helper.remove(PREF_DISPLAY_NAME)
    }

    private inner class SharedPreferencesHelper constructor(
        private val context: Context
    ) {
        fun save(saveKey: String?, string: String?) {
            val editor = prefs.edit()
            editor.putString(saveKey, string)
            editor.apply()
        }

        fun loadString(loadKey: String): String? {
            return prefs.getString(loadKey, null)
        }

        fun loadString(key: String, defaultValue: String = ""): String {
            val value = loadString(key)
            return value ?: defaultValue
        }

        fun remove(removeKey: String?) {
            val editor = prefs.edit()
            editor.remove(removeKey)
            editor.apply()
        }
    }
}