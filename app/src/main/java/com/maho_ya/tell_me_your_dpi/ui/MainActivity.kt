package com.maho_ya.tell_me_your_dpi.ui

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.ActivityMainBinding
import com.maho_ya.tell_me_your_dpi.util.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    // Tap ActionBar
    // https://developer.android.com/reference/android/app/Activity#onOptionsItemSelected(android.view.MenuItem)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            // User chose the "Settings" item, show the app settings UI...
            onBackPress()
            true
        }
        else -> false
    }

    // Tap Back button
    private fun onBackPress() {
        findNavController(R.id.nav_host_fragment).run {
            when (currentDestination!!.id) {
                // タブごとの最初の画面
                R.id.navHome -> finish()
                // それ以外の画面
                else -> popBackStack()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashScreenを使用するのに必要
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerToolbar(savedInstanceState)
        registerBottomNavigation()

        NotificationUtils.createNotificationChannel(this)
    }

    private fun registerToolbar(savedInstanceState: Bundle?) {

        // TODO Consider recreated Activity.
        // Add Toolbar to Activity.
        // https://developer.android.com/training/appbar/setting-up?hl=ja#add-toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        if (savedInstanceState == null) toolbar.title = ""

        setSupportActionBar(toolbar)
    }

    private fun registerBottomNavigation() {

        // https://developer.android.com/guide/navigation/navigation-getting-started?hl=ja#navigate
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // AppBarConfiguration.java で navController.graph を引数に渡すと、TopLevelのidを取得し渡す　
        // TopLevelにはAppIconを付けない。 navGraph の他にR.idでも渡せる
        // public Builder(@NonNull NavGraph navGraph) {
        //    mTopLevelDestinations.add(NavigationUI.findStartDestination(navGraph).getId());
        // }

        val configuration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, configuration)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val logo = findViewById<ImageView>(R.id.logo)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navHome) {
                logo.visibility = View.VISIBLE
                bottomNavigationView.visibility = View.VISIBLE
            } else {
                logo.visibility = View.GONE
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

        setupWithNavController(bottomNavigationView, navController)
    }
}
