package com.maho_ya.tell_me_your_dpi.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme

private val topBarHeight = 84.dp
private val bottomBarHeight = 64.dp

// 引数をcomposableにしてPreviewでHomeとかを渡せば良さそう
@Composable
fun TdpiApp(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: (@Composable () -> Unit)? = null
) {
    AppTheme {
        // https://google.github.io/accompanist/systemuicontroller/
        // StatusBarを透明にする。darkIconsがtrueだと黒いアイコンが表示される
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }

        val tabs = remember { BottomTabs.values() }
        val navController = rememberNavController()
        Scaffold(
            topBar = { TopBar(navController) },
            bottomBar = { BottomBar(navController = navController, tabs = tabs) },
            scaffoldState = scaffoldState
        ) { paddingValues ->
            // paddingValuesでSystemBar＋AppBar+NavigationBarのサイズを取得する
            // 表示のずれが生じるため、contentのコンテナに対してpaddingを設定する必要がある（設定しないと一部隠れる）
            val modifier = Modifier.padding(paddingValues)
            if (content == null) {
                TdpiNagGraph(
                    modifier = modifier,
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            } else {
                Column(modifier) {
                    content()
                }
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight)
            .background(MaterialTheme.colors.primaryVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (currentDestination?.route == BottomTabs.HOME.route) {
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
fun BottomBar(navController: NavController, tabs: Array<BottomTabs>) {
    // https://developer.android.com/jetpack/compose/navigation?hl=ja#bottom-nav
    // 現在のNavDestinationにアクセスできるようにする
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // バックスタックのルートを取得
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomTabs.HOME.route

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
                icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                label = { Text(stringResource(tab.title)) },
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

/**
 * BottomNavigationのアイテム
 */
enum class BottomTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    HOME(R.string.menu_home, R.drawable.ic_baseline_home, TdpiDestinations.HOME_ROUTE),
    RELEASE_NOTE(R.string.menu_release, R.drawable.ic_baseline_new_releases, TdpiDestinations.RELEASE_NOTE_ROUTE),
    ABOUT_APP(R.string.menu_about_app, R.drawable.ic_baseline_about, TdpiDestinations.ABOUT_APP_ROUTE)
}

