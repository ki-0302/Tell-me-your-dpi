package com.maho_ya.tell_me_your_dpi.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.maho_ya.tell_me_your_dpi.BuildConfig
import com.maho_ya.tell_me_your_dpi.util.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashScreenを使用するのに必要
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val adView = try {
            MobileAds.initialize(this) {}
            val adView = AdView(this)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = BuildConfig.AD_UNIT_ID
            adView.loadAd(AdRequest.Builder().build())
            adView
        } catch (e: Exception) {
            Timber.d(e)
            null
        }

        // SystemBarの背景にコンテンツを表示する
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TdpiApp(adView = adView)
        }

        // 通知チャネルの作成
        NotificationUtils.createNotificationChannel(this)
    }
}
