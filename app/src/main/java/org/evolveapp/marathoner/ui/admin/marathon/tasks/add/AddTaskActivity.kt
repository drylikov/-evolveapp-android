package org.evolveapp.marathoner.ui.admin.marathon.tasks.add

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import dagger.hilt.android.AndroidEntryPoint
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityAddTaskBinding
import org.evolveapp.marathoner.ui.admin.marathon.tasks.add.repeat.RepeatTaskOptionsFragment
import org.evolveapp.marathoner.utils.RuntimeLocaleChanger

// TODO: 10/14/2021 API integrations

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    private val viewModel by viewModels<AddTaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.close.setOnClickListener { onBackPressed() }

        binding.layoutRepeat.setOnClickListener {
            RepeatTaskOptionsFragment.newInstance("", "").show(supportFragmentManager, "")
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}