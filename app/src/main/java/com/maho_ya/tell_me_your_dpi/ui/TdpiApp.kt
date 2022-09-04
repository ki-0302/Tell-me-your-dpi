package com.maho_ya.tell_me_your_dpi.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.maho_ya.tell_me_your_dpi.BuildConfig
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors

private val topBarHeight = 84.dp
private val bottomBarHeight = 64.dp

/**
 * AppRoute
 * @param scaffoldState
 * @param hasAppBar Preview用
 * @param content Preview用
 */
@Composable
fun TdpiApp(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    hasAppBar: Boolean = true,
    adView: AdView? = null,
    content: (@Composable () -> Unit)? = null
) {
    AppTheme {
        // https://google.github.io/accompanist/systemuicontroller/
        // StatusBarを透明にする。darkIconsがtrueだと黒いアイコンが表示される
        val systemUiController = rememberSystemUiController()
        // val useDarkIcons = MaterialTheme.colors.isLight

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
        }

        val tabs = remember {
            listOf(Screen.Home, Screen.ReleaseNote, Screen.AboutApp)
        }
        val navController = rememberNavController()

        Scaffold(
            topBar = { if (hasAppBar) TopBar(navController) },
            bottomBar = { if (hasAppBar) BottomBar(navController = navController, tabs = tabs) },
            scaffoldState = scaffoldState
        ) { paddingValues ->
            // paddingValuesでSystemBar＋AppBar+NavigationBarのサイズを取得する
            // 表示のずれが生じるため、contentのコンテナに対してpaddingを設定する必要がある（設定しないと一部隠れる）
            val modifier = Modifier.padding(paddingValues)

            if (content != null) {
                Column(modifier) {
                    content()
                }
                return@Scaffold
            }

            Column {
                adView?.let {
                    AdMobBanner(adView = it)
                }
                TdpiNagGraph(
                    modifier = modifier,
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route == Screen.Tutorial.PostNotifications.route) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight)
            .background(MaterialTheme.colors.primaryVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (currentDestination?.route == Screen.Home.route) {
            Image(
                painter = painterResource(id = AppTheme.images.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    // WindowInsetsから取得したStatusBar分のpaddingを上に追加
                    // ここではロゴがStatusBarに被らないようにしている
                    .statusBarsPadding()
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun BottomBar(navController: NavController, tabs: List<Screen>) {
    // https://developer.android.com/jetpack/compose/navigation?hl=ja#bottom-nav
    // 現在のNavDestinationにアクセスできるようにする
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // バックスタックのルートを取得
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route == Screen.Tutorial.PostNotifications.route) return

    BottomNavigation(
        // BottomNavigationの高さを設定
        // WindowCompat.setDecorFitsSystemWindowsでSystemBarの背景にコンテンツを表示する場合に必要
        modifier = Modifier.windowInsetsBottomHeight(
            WindowInsets.navigationBars.add(WindowInsets(bottom = bottomBarHeight))
        ),
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        tabs.forEach { tab ->
            BottomNavigationItem(
                icon = { tab.iconId?.let { Icon(painterResource(it), contentDescription = null) } },
                label = { Text(stringResource(tab.titleId)) },
                selected = currentRoute == tab.route,
                onClick = {
                    if (tab.route != currentRoute) {
                        navController.navigate(tab.route) {
                            // saveState = trueで、後で復元した場合にremember以外をViewModelから復元する
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true // 上に同じ画面をスタックしないようにする
                            restoreState = true // saveStateで保存したものを復元する
                        }
                    }
                },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colors.onSecondary,
                unselectedContentColor = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}

@Composable
fun AdMobBanner(
    darkTheme: Boolean = isSystemInDarkTheme(),
    adView: AdView
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(10.dp),
        factory = {
            adView
        },
    )

    Divider(color = if (darkTheme) Colors.Gray555 else Colors.GrayAAA)
}

/**
 * NavigationGraphの遷移先
 */
sealed class Screen(val route: String, @DrawableRes val iconId: Int?, @StringRes val titleId: Int) {
    object Home : Screen("home", R.drawable.ic_baseline_home, R.string.menu_home)
    object ReleaseNote : Screen(
        "release_note",
        R.drawable.ic_baseline_new_releases,
        R.string.menu_release
    )

    object AboutApp : Screen("about_app", R.drawable.ic_baseline_about, R.string.menu_about_app)

    sealed class Tutorial(route: String, @DrawableRes iconId: Int?, @StringRes titleId: Int) : Screen(
        route, iconId, titleId
    ) {
        @Suppress("unused")
        object TutorialRoot : Tutorial(
            "tutorial",
            null,
            R.string.title_tutorial_post_notifications
        )

        object PostNotifications : Tutorial(
            "tutorial_post_notifications",
            null,
            R.string.title_tutorial_post_notifications
        )
    }
}
