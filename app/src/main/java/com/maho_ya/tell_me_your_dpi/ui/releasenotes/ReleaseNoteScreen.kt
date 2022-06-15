package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
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
import com.maho_ya.tell_me_your_dpi.BuildConfig
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors

private val defaultSpacerSize = 16.dp
private val spacerSizeBetweenAppVersionAndDate = 8.dp

@Composable
fun ReleaseNoteContent(
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
            ReleaseNoteContentItem(
                modifier = modifier,
                // リソースの取得
                // https://developer.android.com/jetpack/compose/resources?hl=ja#strings
                releaseNote = ReleaseNote(
                    appVersion = "1.0",
                    date = "2020.11.11",
                    description = "test"
                )
            )
        }
    }
}

@Composable
fun ReleaseNoteContentItem(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    releaseNote: ReleaseNote
) {
    Column(modifier = modifier.padding(defaultSpacerSize)) {
        // Version
        Text(
            text = stringResource(
                R.string.release_notes_item_version,
                releaseNote.appVersion
            ),
            style = MaterialTheme.typography.h5,
            modifier = modifier.fillMaxWidth()
        )

        // Date
        Text(
            text = releaseNote.date,
            style = MaterialTheme.typography.subtitle2,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        // Description
        Text(
            text = releaseNote.description,
            style = MaterialTheme.typography.body1,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 3.dp),
        )
    }

    Divider(color = if (darkTheme) Colors.Gray333 else Colors.GrayAAA)
}

@Composable
fun LoadingContent() {

}


/**
 * リリースノートを表示
 * @param modifier modifier
 * @param state 現在表示中のスクロール可能なアイテムの制御・監視が行える状態オブジェクトを保存したもの
 */
@Composable
fun ReleaseNotesScreen(
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
        Row(modifier.fillMaxSize()) {
            ReleaseNoteContent(
                modifier = modifier,
                darkTheme = darkTheme,
                state = state
            )
        }
    }
}

@Preview("Release Note screen")
@Preview("Release Note (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReleaseNotesScreen() {
    AppTheme {
        ReleaseNotesScreen()
    }
}