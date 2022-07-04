package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.ui.TdpiApp
import com.maho_ya.tell_me_your_dpi.ui.theme.Colors
import com.maho_ya.tell_me_your_dpi.ui.theme.PreviewDefault

private val defaultSpacerSize = 16.dp
private val spacerSizeBetweenAppVersionAndDate = 8.dp
private val spacerSizeBetweenDateAndDescription = 3.dp
private val errorMessagePadding = 20.dp

@Composable
private fun ReleaseNoteContent(
    modifier: Modifier = Modifier,
    uiState: ReleaseNoteUiState,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit = {}
) {
    LoadingReleaseNoteContent(
        isLoading = uiState.isLoading,
        onRefresh = onRefresh,
        content = {
            if (uiState.userMessageId != null) {
                ReleaseNoteErrorContent(
                    modifier = modifier,
                    userMessageId = uiState.userMessageId,
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
private fun ReleaseNoteContentList(
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
        itemsIndexed(uiState.items) { index, item ->
            ReleaseNoteContentItem(
                modifier = Modifier,
                // リソースの取得
                // https://developer.android.com/jetpack/compose/resources?hl=ja#strings
                releaseNote = item,
                hasDivider = index < uiState.items.size - 1
            )
        }
    }
}

@Composable
private fun ReleaseNoteContentItem(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    releaseNote: ReleaseNote,
    hasDivider: Boolean = true
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

    if (hasDivider) Divider(color = if (darkTheme) Colors.Gray555 else Colors.GrayAAA)
}

@Composable
private fun ReleaseNoteErrorContent(
    modifier: Modifier = Modifier,
    @StringRes userMessageId: Int?,
    onClick: () -> Unit,
) {
    if (userMessageId == null) return

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier.padding(errorMessagePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(userMessageId),
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
            Spacer(Modifier.size(100.dp))
        }
    }
}

@Composable
private fun LoadingReleaseNoteContent(
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
private fun ReleaseNotesScreen(
    modifier: Modifier = Modifier,
    uiState: ReleaseNoteUiState,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit = {}
) {
    ReleaseNoteContent(
        modifier = modifier,
        uiState = uiState,
        listState = listState,
        onRefresh = onRefresh
    )
}

@Composable
fun ReleaseNoteRoute(
    releaseNotesViewModel: ReleaseNotesViewModel = viewModel()
) {
    // collectAsState() でuiStateをリスナーし、値が変わると再コンポーズするようになる
    val uiState by releaseNotesViewModel.uiState.collectAsState()
    // スクロール位置やアイテムのレイアウトの変更を検知できる
    val listState: LazyListState = rememberLazyListState()

    ReleaseNotesScreen(
        uiState = uiState,
        listState = listState,
        onRefresh = { releaseNotesViewModel.onReloadClicked() }
    )
}

@Preview("Release Note screen")
@Preview("Release Note screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@PreviewDefault
@Composable
private fun PreviewReleaseNotesScreen() {
    val uiState = ReleaseNoteUiState(
        items = listOf(
            ReleaseNote(date = "2022/06/15", "1.1.0", "アップデートしました"),
            ReleaseNote(date = "2022/06/13", "1.0.0", "リリースしました"),
        )
    )

    TdpiApp() {
        ReleaseNotesScreen(uiState = uiState)
    }
}

@Preview("Release Note Error Content")
@Preview("Release Note Error Content(dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@PreviewDefault
@Composable
private fun PreviewReleaseNoteErrorContent() {
    val uiState = ReleaseNoteUiState(
        userMessageId = R.string.network_error_title
    )

    TdpiApp() {
        ReleaseNotesScreen(uiState = uiState)
    }
}
