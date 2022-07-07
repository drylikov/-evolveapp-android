package org.evolveapp.marathoner.ui.main.profile.guest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.evolveapp.domain.models.login.SocialLoginResponse
import org.evolveapp.domain.models.login.request.LogInRequest
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentGuestProfileBinding
import org.evolveapp.marathoner.ui.splash.SplashActivity
import org.evolveapp.marathoner.utils.goTo
import org.evolveapp.marathoner.utils.launchViewLifecycleScope
import org.evolveapp.marathoner.utils.showToast
import timber.log.Timber

@AndroidEntryPoint
class GuestProfileFragment : Fragment() {

    private val viewModel by viewModels<GuestViewModel>()

    private val registerByGoogleContract = registerForActivityResult(RegisterByGoogleContract()) {

        handleGoogleSignInResult(it)
    }

    private var binding: FragmentGuestProfileBinding? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_guest_profile, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRegistrationByGoogle()

    }

    private fun initRegistrationByGoogle() {

        binding?.registerByGoogle?.setOnClickListener {

            val clientId = getString(R.string.default_web_client_id)

            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .requestScopes(Scope(Scopes.EMAIL))
                .requestServerAuthCode(clientId)
                .build()

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            updateUI(account)

        }

    }

    private fun updateUI(account: GoogleSignInAccount?) {

        when (account) {

            null -> {
                val signInIntent: Intent? = mGoogleSignInClient?.signInIntent
                registerByGoogleContract.launch(signInIntent)
            }

            else -> {
                // User already signed in
                Timber.d("GoogleSignInAccount not null")

                lifecycleScope.launchWhenCreated {

                    val accessToken = AuthUtils.obtainAccessToken(
                        context = requireContext(),
                        googleSignInAccount = account
                    )

                    val logInRequest = viewModel.generateLoginRequest(
                        loginProvider = "google",
                        userEmail = account.email ?: "",
                        expiresIn = 1633440430,
                        accessToken = accessToken,
                        userId = account.id ?: ""
                    )

                    socialLoginByGoogle(logInRequest)

                }

            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {

        task.addOnSuccessListener { googleSignInAccount ->

            showToast(R.string.registration_done_successfully)

            launchViewLifecycleScope {

                val accessToken = AuthUtils.obtainAccessToken(
                    context = requireContext(),
                    googleSignInAccount = googleSignInAccount
                )

                Timber.d("accessToken: $accessToken")

                val logInRequest = viewModel.generateLoginRequest(
                    loginProvider = "google",
                    userEmail = googleSignInAccount.email ?: "",
                    expiresIn = 1633440430,
                    accessToken = accessToken,
                    userId = googleSignInAccount.id ?: ""
                )

                socialLoginByGoogle(logInRequest)

            }


        }

        task.addOnFailureListener { e ->
            Timber.e(e)
            showToast(R.string.something_went_wrong_try_again_later)
        }

    }

    private fun socialLoginByGoogle(logInRequest: LogInRequest) {

        launchViewLifecycleScope {

            viewModel.login(logInRequest).collect {

                it.onSuccess { response: SocialLoginResponse ->
                    viewModel.saveUserInfo(response)
                    requireActivity().goTo(SplashActivity::class.java)
                    requireActivity().finishAffinity()
                }

                it.onFailure { message, code ->
                    Timber.e(message)
                    showToast(R.string.something_went_wrong_try_again_later)
                }

            }

        }
    }

}