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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors

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
fun AboutContent(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    state: LazyListState = rememberLazyListState()
) {
    // contextを取得
    // https://developer.android.google.cn/jetpack/compose/interop/interop-apis?hl=ja#composition-locals
    val context = LocalContext.current

    // 多数のアイテムを表示する必要がある場合に使用する
    // 表示するアイテムのみコンポーズする、スクロール可能なレイアウト
    // https://developer.android.com/jetpack/compose/lists?hl=ja#lazy
    LazyColumn(
        modifier = modifier,
        state = state,
    ) {
        item {
            AboutContentItem(
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
            AboutContentItem(
                modifier = modifier.clickable {
                    openPrivacyPolicySite(context)
                },
                darkTheme = darkTheme,
                text = stringResource(R.string.about_privacy_policy_title),
                // clickableからは@Composableの関数は呼び出せないため注意
            )
        }
        item {
            AboutContentItem(
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
fun AboutContentItem(
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
    isSystemInDarkTheme()
    Divider(color = if (darkTheme) Colors.Gray333 else Colors.GrayAAA)
}

/**
 * アプリ情報を表示
 * @param modifier modifier
 * @param state 現在表示中のスクロール可能なアイテムの制御・監視が行える状態オブジェクトを保存したもの
 */
@Composable
fun AboutAppScreen(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    state: LazyListState = rememberLazyListState()
) {
    // ScaffoldまたはSurfaceを置くとコンテンツに共通の設定を反映できる。詳細はリンク参照。
    // ScaffoldはTopAppBar, BottomAppBarなどが必要な時に使用する
    // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Surface(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.BorderStroke,androidx.compose.ui.unit.Dp,kotlin.Function0)
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background,
    ) {
        // parentサイズ分のレイアウト
        Row(modifier.fillMaxSize()) {
            AboutContent(
                modifier = modifier,
                darkTheme = darkTheme,
                state = state
            )
        }
    }
}

@Preview("About screen")
@Preview("About screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAboutScreen() {
    AppTheme {
        AboutAppScreen()
    }
}
