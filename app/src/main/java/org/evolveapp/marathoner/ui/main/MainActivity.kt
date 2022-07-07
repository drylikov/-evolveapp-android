package org.evolveapp.marathoner.ui.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.ActivityMainBinding
import org.evolveapp.marathoner.ui.main.profile.ProfileUtils
import org.evolveapp.marathoner.utils.RuntimeLocaleChanger
import org.evolveapp.marathoner.utils.prefDao

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ProfileUtils.resolveUpdatingUserData(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        resolveDestinationChangedListener()
        initBottomNavClickListeners()

        handleBottomNavProfileButton()

    }

    private fun handleBottomNavProfileButton() {

        val url = if (prefDao.accessToken.isNullOrBlank()) {
            R.drawable.ic_profile_userpic
        } else {
            prefDao.userPhoto
        }

        Glide.with(this).load(url)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_profile_userpic)
            .error(R.drawable.ic_profile_userpic)
            .into(binding.bottomNav.profilePhoto)

    }

    private fun initBottomNavClickListeners() {
        binding.bottomNav.marathon.setOnClickListener {
            navController.navigate(R.id.marathonsFragment)
        }

        binding.bottomNav.search.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        binding.bottomNav.profile.setOnClickListener {

            val id = if (prefDao.accessToken.isNullOrBlank()) {
                R.id.guestProfileFragment
            } else {
                R.id.profileFragment
            }

            navController.navigate(id)

        }
    }

    private fun resolveDestinationChangedListener() {

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val activeColor = ContextCompat.getColor(this, R.color.colorPrimary)

            when (destination.id) {

                R.id.marathonsFragment -> {
                    binding.bottomNav.marathon.setColorFilter(activeColor)
                    binding.bottomNav.search.setColorFilter(Color.BLACK)
                    binding.bottomNav.profile.setStrokeColor(ColorStateList.valueOf(Color.TRANSPARENT))
                }

                R.id.searchFragment -> {
                    binding.bottomNav.marathon.setColorFilter(Color.BLACK)
                    binding.bottomNav.search.setColorFilter(activeColor)
                    binding.bottomNav.profile.setStrokeColor(ColorStateList.valueOf(Color.TRANSPARENT))
                }

                R.id.profileFragment, R.id.guestProfileFragment -> {
                    binding.bottomNav.marathon.setColorFilter(Color.BLACK)
                    binding.bottomNav.search.setColorFilter(Color.BLACK)
                    binding.bottomNav.profile.setStrokeColor(ColorStateList.valueOf(activeColor))
                }

            }

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