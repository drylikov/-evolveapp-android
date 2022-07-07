package org.evolveapp.marathoner.ui.participants

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityParticipantsBinding
import org.evolveapp.marathoner.utils.goTo

@AndroidEntryPoint
class ParticipantsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParticipantsBinding

    private val marathonId: String by lazy { intent.getStringExtra(EXTRA_ID) ?: "" }

    private val viewModel by viewModels<ParticipantsViewModel>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.marathonId = marathonId

        binding = DataBindingUtil.setContentView(this, R.layout.activity_participants)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController


    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

    companion object {

        private const val EXTRA_ID = "id"

        @JvmStatic
        fun launch(activity: Activity, id: String) {
            activity.goTo(ParticipantsActivity::class.java) {
                putExtra(EXTRA_ID, id)
            }
        }

    }

}