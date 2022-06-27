package com.maho_ya.tell_me_your_dpi.ui.aboutapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.maho_ya.tell_me_your_dpi.BuildConfig
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.ui.TdpiApp
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors
import com.maho_ya.tell_me_your_dpi.ui.theme.PreviewDefault

private val defaultSpacerSize = 20.dp
private const val privacyPolicyUrl = "https://maho-ya.firebaseapp.com/privacy.html"

private fun openPrivacyPolicySite(context: Context) {
    val color = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> getColorForMOrHigher(context)
        else -> getColorForLessThanM(context)
    }
    val defaultColors = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(color)
        .setNavigationBarColor(color)
        .build()
    val builder = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(defaultColors)
        .build()
    builder.launchUrl(context, Uri.parse(privacyPolicyUrl))
}

@RequiresApi(Build.VERSION_CODES.M)
private fun getColorForMOrHigher(context: Context) =
    context.resources.getColor(R.color.color_primary, context.theme)

@Suppress("DEPRECATION")
private fun getColorForLessThanM(context: Context) =
    context.resources.getColor(R.color.color_primary)

private fun openOssLicences(context: Context) {
    val intent = Intent(context, OssLicensesMenuActivity::class.java)
    intent.putExtra("title", context.getString(R.string.about_oss_licences_title))
    context.startActivity(intent)
}

@Composable
fun AboutAppContent(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    listState: LazyListState = rememberLazyListState(),
) {
    // contextを取得
    // https://developer.android.google.cn/jetpack/compose/interop/interop-apis?hl=ja#composition-locals
    val context = LocalContext.current

    // 多数のアイテムを表示する必要がある場合に使用する
    // 表示するアイテムのみコンポーズする、スクロール可能なレイアウト
    // https://developer.android.com/jetpack/compose/lists?hl=ja#lazy
    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        item {
            AboutAppContentItem(
                modifier = modifier,
                // リソースの取得
                // https://developer.android.com/jetpack/compose/resources?hl=ja#strings
                text = stringResource(
                    R.string.about_version_title,
                    BuildConfig.VERSION_NAME
                )
            )
        }
        item {
            AboutAppContentItem(
                modifier = modifier.clickable {
                    openPrivacyPolicySite(context)
                },
                darkTheme = darkTheme,
                text = stringResource(R.string.about_privacy_policy_title),
                // clickableからは@Composableの関数は呼び出せないため注意
            )
        }
        item {
            AboutAppContentItem(
                modifier = modifier.clickable {
                    openOssLicences(context)
                },
                darkTheme = darkTheme,
                text = stringResource(R.string.about_oss_licences_title),
            )
        }
    }
}

@Composable
fun AboutAppContentItem(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = defaultSpacerSize,
                vertical = defaultSpacerSize
            ),
    )
    Divider(color = if (darkTheme) Colors.Gray555 else Colors.GrayAAA)
}

/**
 * アプリ情報を表示
 */
@Composable
fun AboutAppScreen(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    listState: LazyListState = rememberLazyListState()
) {
    // parentサイズ分のレイアウト
    Column(modifier.fillMaxSize()) {
        AboutAppContent(
            darkTheme = darkTheme,
            listState = listState
        )
    }
}

@Composable
fun AboutAppRoute() {
    // スクロール位置やアイテムのレイアウトの変更を検知できる
    val listState: LazyListState = rememberLazyListState()

    AboutAppScreen(
        listState = listState,
    )
}

@Preview("About screen")
@Preview("About screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@PreviewDefault
@Composable
fun PreviewAboutScreen() {
    TdpiApp() {
        AboutAppScreen()
    }
}
