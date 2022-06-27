package com.maho_ya.tell_me_your_dpi.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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
    }
}
