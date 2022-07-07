package org.evolveapp.marathoner.ui.admin.marathon.tasks.manage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityManageTaskBinding
import org.evolveapp.marathoner.utils.OnClick

class ManageTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageTaskBinding

    private lateinit var tabsAdapter: TabsAdapter

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_task)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController


        initTabsUI(
            listOf(
                Tab(R.id.answersFragment, "Новые", true),
                Tab(R.id.approvedFragment, "Одобренные", false),
                Tab(R.id.adminDiscussionsFragment, "Обсуждения 23", true),
                Tab(R.id.notSendFragment, "Не прислали", false),
            )
        )
    }

    private val onTabClick = OnClick { tab: Tab, position: Int ->

        binding.tabs.smoothScrollToPosition(position)

        navController.navigate(tab.id)

    }

    private fun initTabsUI(tabs: List<Tab>) {

        tabsAdapter = TabsAdapter(tabs, onTabClick)

        binding.tabs.apply {
            layoutManager =
                LinearLayoutManager(this@ManageTaskActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = tabsAdapter
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim)
    }

}