package org.evolveapp.marathoner.ui.marathon

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityMarathonBinding
import org.evolveapp.marathoner.ui.marathon.details.notifications.NotificationsFragment
import org.evolveapp.marathoner.ui.participants.ParticipantsActivity
import org.evolveapp.marathoner.utils.goTo
import org.evolveapp.marathoner.utils.simulateLoadingData

@AndroidEntryPoint
class MarathonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarathonBinding

    private lateinit var navController: NavController

    private val marathonId: String by lazy { intent.getStringExtra(EXTRA_ID) ?: "" }

    private val viewModel by viewModels<MarathonViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_marathon)

        viewModel.marathonId = marathonId

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController


        initListeners()

        simulateLoadingData(
            shimmerLayout = binding.shimmer,
            content = binding.content
        )

    }

    private fun initListeners() {

        binding.btnMain.setOnClickListener {
            navController.navigate(R.id.marathonDetailsFragment)
        }

        binding.back.setOnClickListener { onBackPressed() }
        binding.more.setOnClickListener {
            NotificationsFragment().show(
                supportFragmentManager,
                null
            )
        }

        binding.details.layoutParticipants.setOnClickListener {
            ParticipantsActivity.launch(this, marathonId)
        }

    }

    private fun transparentStatusBar() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }


    companion object {

        private const val EXTRA_ID = "id"

        @JvmStatic
        fun launch(activity: Activity, id: String) {
            activity.goTo(MarathonActivity::class.java) {
                putExtra(EXTRA_ID, id)
            }
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}