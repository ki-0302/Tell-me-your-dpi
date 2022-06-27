package com.maho_ya.tell_me_your_dpi.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.util.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashScreenを使用するのに必要
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // SystemBarの背景にコンテンツを表示する
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TdpiApp()
        }

        //NotificationUtils.createNotificationChannel(this)
    }
}
