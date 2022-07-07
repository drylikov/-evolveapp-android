package org.evolveapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PrefDao private constructor(private val pref: SharedPreferences) {

    var language: String
        get() = pref.getString(KEY_LANGUAGE, LANGUAGE_EN).toString()
        set(value) = pref.edit(commit = true) { putString(KEY_LANGUAGE, value) }

    var accessToken: String?
        get() = pref.getString(KEY_ACCESS_TOKEN, null)
        set(value) = pref.edit(commit = true) { putString(KEY_ACCESS_TOKEN, value) }

    var refreshToken: String?
        get() = pref.getString(KEY_REFRESH_TOKEN, null)
        set(value) = pref.edit(commit = true) { putString(KEY_REFRESH_TOKEN, value) }

    var username: String?
        get() = pref.getString(KEY_USERNAME, null)
        set(value) = pref.edit(commit = true) { putString(KEY_USERNAME, value) }

    var firstName: String?
        get() = pref.getString(KEY_USER_FIRST_NAME, null)
        set(value) = pref.edit(commit = true) { putString(KEY_USER_FIRST_NAME, value) }

    var lastName: String?
        get() = pref.getString(KEY_USER_LAST_NAME, null)
        set(value) = pref.edit(commit = true) { putString(KEY_USER_LAST_NAME, value) }

    var userEmail: String?
        get() = pref.getString(KEY_USER_EMAIL, null)
        set(value) = pref.edit(commit = true) { putString(KEY_USER_EMAIL, value) }

    var userPhoto: String?
        get() = pref.getString(KEY_USER_PHOTO, null)
        set(value) = pref.edit(commit = true) { putString(KEY_USER_PHOTO, value) }

    var dateOfBirth: String?
        get() = pref.getString(KEY_DATE_OF_BIRTH, null)
        set(value) = pref.edit(commit = true) { putString(KEY_DATE_OF_BIRTH, value) }

    var userId: String?
        get() = pref.getString(KEY_USER_ID, null)
        set(value) = pref.edit(commit = true) { putString(KEY_USER_ID, value) }

    var country: String?
        get() = pref.getString(KEY_COUNTRY, null)
        set(value) = pref.edit(commit = true) { putString(KEY_COUNTRY, value) }

    var pushToken: String?
        get() = pref.getString(KEY_PUSH_TOKEN, null)
        set(value) = pref.edit(commit = true) { putString(KEY_PUSH_TOKEN, value) }

    var hiddenProfile: Boolean
        get() = pref.getBoolean(KEY_HIDDEN_PROFILE, false)
        set(value) = pref.edit(commit = true) { putBoolean(KEY_HIDDEN_PROFILE, value) }

    fun clearAll() = pref.edit(commit = true) { clear() }

    companion object {

        private const val PREF_DAO_FILE_NAME = "PreferenceDataFile"

        const val LANGUAGE_RU = "ru"
        const val LANGUAGE_EN = "en"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_PUSH_TOKEN = "push_token"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_FIRST_NAME = "user_first_name"
        private const val KEY_USER_LAST_NAME = "user_last_name"
        private const val KEY_USERNAME = "username"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHOTO = "user_photo"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_DATE_OF_BIRTH = "date_of_birth"
        private const val KEY_COUNTRY = "country"
        private const val KEY_HIDDEN_PROFILE = "hidden_profile"


        fun getInstance(context: Context) = PrefDao(
            context.getSharedPreferences(
                PREF_DAO_FILE_NAME,
                Context.MODE_PRIVATE
            )
        )


    }

}