package org.evolveapp.marathoner.ui.main.profile.guest

import android.content.Context
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.evolveapp.domain.models.login.request.Data
import org.evolveapp.domain.models.login.request.LogInRequest

object AuthUtils {


    suspend fun obtainAccessToken(
        context: Context,
        googleSignInAccount: GoogleSignInAccount,
        scope: String = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE,
    ): String = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
        GoogleAuthUtil.getToken(context, googleSignInAccount.account, scope)
    }

}

fun buildLoginRequest(builder: LogInRequest.() -> Unit) = LogInRequest().apply(builder)

fun buildLoginRequestData(builder: Data.() -> Unit) = Data().apply(builder)
