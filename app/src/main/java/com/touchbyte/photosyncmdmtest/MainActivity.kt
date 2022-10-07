package com.touchbyte.photosyncmdmtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.touchbyte.mdm.MDMManager
import com.touchbyte.photosync.receiver.ReceiverManager
import com.touchbyte.photosyncmdmtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val _restrictionsReceiver: BroadcastReceiver = RestrictionsReceiver()

    private inner class RestrictionsReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "managed configuration changed")
            MDMManager.handleManagedConfigurations()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    override fun onResume() {
        super.onResume()

        val restrictionsFilter = IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED)
        if (!ReceiverManager.isReceiverRegistered(_restrictionsReceiver)) ReceiverManager.registerReceiver(_restrictionsReceiver, restrictionsFilter)
    }

    override fun onPause() {
        super.onPause()

        ReceiverManager.unregisterReceiver(_restrictionsReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    init {
        instance = this
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName

        private var instance: MainActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}