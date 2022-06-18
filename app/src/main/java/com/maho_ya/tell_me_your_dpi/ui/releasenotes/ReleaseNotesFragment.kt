package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentReleaseNotesBinding
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReleaseNotesFragment : Fragment(R.layout.fragment_release_notes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReleaseNotesBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    ReleaseNoteRoute()
                }
            }
        }
    }
}

@Composable
fun ReleaseNoteRoute(
    modifier: Modifier = Modifier,
    releaseNotesViewModel: ReleaseNotesViewModel = viewModel()
) {
    // collectAsState() でuiStateをリスナーし、値が変わると再コンポーズするようになる
    val uiState by releaseNotesViewModel.uiState.collectAsState()
    // スクロール位置やアイテムのレイアウトの変更を検知できる
    val listState: LazyListState = rememberLazyListState()

    ReleaseNotesScreen(
        modifier = modifier,
        uiState = uiState,
        listState = listState,
        onRefresh = { releaseNotesViewModel.onReloadClicked() }
    )
}
