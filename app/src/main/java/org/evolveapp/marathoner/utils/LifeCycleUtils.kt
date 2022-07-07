package org.evolveapp.marathoner.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


fun AppCompatActivity.launchLifecycleScope(block: suspend () -> Unit) {

    lifecycleScope.launch {
        block()
    }

}


fun Fragment.launchViewLifecycleScope(block: suspend () -> Unit) {

    viewLifecycleOwner.lifecycleScope.launch {
        block()
    }

}


/*
fun AppCompatActivity.repeatOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend () -> Unit
) {

    // Create a new coroutine since repeatOnLifecycle is a suspend function
    lifecycleScope.launch {
        // The block passed to repeatOnLifecycle is executed when the lifecycle
        // is at least STARTED and is cancelled when the lifecycle is STOPPED.
        // It automatically restarts the block when the lifecycle is STARTED again.
        lifecycle.repeatOnLifecycle(state) {
            // Safely collect from locationFlow when the lifecycle is STARTED
            // and stops collection when the lifecycle is STOPPED
            block()
        }
    }

}


fun Fragment.repeatOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend () -> Unit
) {

    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {

            block()
        }
    }

}*/
