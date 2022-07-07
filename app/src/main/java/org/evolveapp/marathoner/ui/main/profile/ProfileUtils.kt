package org.evolveapp.marathoner.ui.main.profile

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.services.UpdateUserDataService
import org.evolveapp.marathoner.ui.main.profile.settings.model.SocialNetwork
import org.evolveapp.marathoner.utils.flagEmoji
import org.evolveapp.marathoner.utils.prefDao
import java.util.*

object ProfileUtils {

    fun buildUserName(firstName: String?, lastName: String?) = buildString {
        append(capitalizeUserName(firstName))
        append(" ")
        append(capitalizeUserName(lastName))
    }

    fun capitalizeUserName(name: String?): String? {

        if (name.isNullOrBlank()) return name

        val names = name.split(" ")

        var capitalized = ""

        for (i in names.indices) {

            capitalized += names[i].replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            if (i != names.size - 1) {
                capitalized += " "
            }

        }

        return capitalized.trim()

    }

    /**
     * Check if user already logged in and local user data is null, then start "UpdateUserDataService"
     * to update user data
     */
    fun resolveUpdatingUserData(context: Context) {
        context.prefDao.accessToken?.apply {
            if (context.prefDao.username.isNullOrBlank() || context.prefDao.userEmail.isNullOrBlank()) {
                UpdateUserDataService.launch(context)
            }
        }
    }

    fun getDisplayCountryWithLogo(countryCode: String): String {
        val locale = Locale("", countryCode)

        val selectedCountry = buildString {
            append(locale.flagEmoji)
            append(" ")
            append(locale.displayCountry)
        }

        return selectedCountry
    }

    fun getAvailableSocialNetworks(activity: Activity): List<SocialNetwork> {

        val list = arrayListOf<SocialNetwork>()

        val googleAccount = GoogleSignIn.getLastSignedInAccount(activity)

        val isGoogleConnected = googleAccount != null

        val google = SocialNetwork(
            name = R.string.google,
            isConnected = isGoogleConnected,
            logo = R.drawable.logo_google,
        )
        list.add(google)


        // FIXME: 10/12/2021 Handle isConnected param
        val facebook = SocialNetwork(
            name = R.string.facebook,
            isConnected = false,
            logo = R.drawable.logo_fb,
        )
        list.add(facebook)


        // FIXME: 10/12/2021 Handle isConnected param
        val twitter = SocialNetwork(
            name = R.string.twitter,
            isConnected = false,
            logo = R.drawable.logo_twitter,
        )
        list.add(twitter)


        return list
    }

}