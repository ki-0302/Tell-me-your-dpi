package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors

private val defaultSpacerSize = 16.dp
private val spacerSizeBetweenAppVersionAndDate = 8.dp
private val spacerSizeBetweenDateAndDescription = 3.dp
private val errorMessagePadding = 20.dp

@Composable
fun ReleaseNoteContent(
    modifier: Modifier = Modifier,
    uiState: ReleaseNoteUiState,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit
) {
    LoadingContent(
        isLoading = uiState.isLoading,
        onRefresh = onRefresh,
        content = {
            if (uiState.userMessage != null) {
                ReleaseNoteErrorContent(
                    modifier = modifier,
                    userMessage = uiState.userMessage,
                    onClick = onRefresh
                )
            } else {
                ReleaseNoteContentList(
                    modifier = modifier,
                    uiState = uiState,
                    listState = listState
                )
            }
        }
    )
}

@Composable
fun ReleaseNoteContentList(
    modifier: Modifier = Modifier,
    uiState: ReleaseNoteUiState,
    listState: LazyListState = rememberLazyListState(),
) {
    // 多数のアイテムを表示する必要がある場合に使用する
    // 表示するアイテムのみコンポーズする、スクロール可能なレイアウト
    // https://developer.android.com/jetpack/compose/lists?hl=ja#lazy
    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        items(uiState.items) { item ->
            ReleaseNoteContentItem(
                modifier = modifier,
                // リソースの取得
                // https://developer.android.com/jetpack/compose/resources?hl=ja#strings
                releaseNote = item
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
                .padding(top = spacerSizeBetweenAppVersionAndDate),
        )

        // Description
        Text(
            text = releaseNote.description,
            style = MaterialTheme.typography.body1,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = spacerSizeBetweenDateAndDescription),
        )
    }

    Divider(color = if (darkTheme) Colors.Gray333 else Colors.GrayAAA)
}

@Composable
fun ReleaseNoteErrorContent(
    modifier: Modifier = Modifier,
    @StringRes userMessage: Int?,
    onClick: () -> Unit,
) {
    if (userMessage == null) return

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier.padding(errorMessagePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(userMessage),
                style = MaterialTheme.typography.body1,
                modifier = modifier.padding(defaultSpacerSize),
            )
            Button(
                modifier = modifier,
                onClick = onClick
            ) {
                Text(
                    text = stringResource(R.string.network_error_reload),
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(spacerSizeBetweenDateAndDescription)
                )
            }
        }
    }
}

@Preview("Release Note Error Content")
@Preview("Release Note Error Content(dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReleaseNoteErrorContent() {
    val uiState = ReleaseNoteUiState(
        userMessage = R.string.network_error_title
    )

    AppTheme {
        ReleaseNotesScreen(
            uiState = uiState,
            onRefresh = {}
        )
    }
}

@Composable
fun LoadingContent(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    // https://google.github.io/accompanist/swiperefresh/
    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = onRefresh,
        content = content
    )
}

/**
 * リリースノートを表示。スクリーンレベルComposable（ルートとなるComposable）
 * ViewModelはスクリーンレベルのみに渡す。
 * @param modifier modifier
 * @param uiState UIの状態
 * @param listState 現在表示中のスクロール可能なアイテムの制御・監視が行える状態オブジェクトを保存したもの
 * @param onRefresh リロード用の高階関数
 */
@Composable
fun ReleaseNotesScreen(
    modifier: Modifier = Modifier,
    uiState: ReleaseNoteUiState,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit
) {
    // ScaffoldまたはSurfaceを置くとコンテンツに共通の設定を反映できる。詳細はリンク参照。
    // ScaffoldはTopAppBar, BottomAppBarなどが必要な時に使用する
    // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Surface(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.BorderStroke,androidx.compose.ui.unit.Dp,kotlin.Function0)
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background,
    ) {
        Row(
            modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReleaseNoteContent(
                modifier = modifier,
                uiState = uiState,
                listState = listState,
                onRefresh = onRefresh
            )
        }
    }
}

@Preview("Release Note screen")
@Preview("Release Note (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReleaseNotesScreen() {
    val uiState = ReleaseNoteUiState(
        items = listOf(
            ReleaseNote(date = "2022/06/15", "1.1.0", "アップデートしました"),
            ReleaseNote(date = "2022/06/13", "1.0.0", "リリースしました"),
        )
    )

    AppTheme {
        ReleaseNotesScreen(
            uiState = uiState,
            onRefresh = {}
        )
    }
}
