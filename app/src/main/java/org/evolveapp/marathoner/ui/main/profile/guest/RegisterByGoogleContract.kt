package org.evolveapp.marathoner.ui.main.profile.guest

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

class RegisterByGoogleContract : ActivityResultContract<Intent, Task<GoogleSignInAccount>>() {

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount> {

        return GoogleSignIn.getSignedInAccountFromIntent(intent)

    }
}