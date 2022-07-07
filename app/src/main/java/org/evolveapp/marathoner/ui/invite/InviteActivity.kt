package org.evolveapp.marathoner.ui.invite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityInviteBinding

class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)

        binding.back.setOnClickListener { onBackPressed() }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}