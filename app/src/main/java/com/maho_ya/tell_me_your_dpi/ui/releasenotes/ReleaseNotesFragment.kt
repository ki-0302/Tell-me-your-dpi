package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentReleaseNotesBinding
import com.maho_ya.tell_me_your_dpi.ui.aboutapp.AboutAppScreen
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReleaseNotesFragment : Fragment(R.layout.fragment_release_notes) {

    private val releaseNotesViewModel: ReleaseNotesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReleaseNotesBinding.bind(view)
        binding.viewModel = releaseNotesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    ReleaseNotesScreen()
                }
            }
        }
    }
}

// fragment_release_notes.xml のRecyclerViewにカスタムオプションを定義している。
// 自動的に該当するBindingAdapter（今回はこのメソッド）を探し出し実行する
// object内に宣言する場合は @JvmStatic もつける
@BindingAdapter(value = ["releaseNotes", "hasError"])
fun setReleaseNotesItems(
    recyclerView: RecyclerView,
    releaseNotes: List<com.maho_ya.tell_me_your_dpi.model.ReleaseNote>?,
    hasError: Boolean
) {

    releaseNotes ?: return

    if (hasError) return

    // adapterがまだ設定されていなければ設定する
    if (recyclerView.adapter == null) {
        recyclerView.adapter = ReleaseNotesAdapter()
    }

    // SubmitList method of ListAdapter submit a new list to be diffed, and displayed.
    // 新しく表示するリストをsubmitListで登録する。すでに表示している場合は差分を計算して表示してくれる
    (recyclerView.adapter as ReleaseNotesAdapter).submitList(releaseNotes)

    recyclerView.apply {
        // Add separate line.
        addItemDecoration(
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        )

        setHasFixedSize(true)
    }
}
