package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maho_ya.tell_me_your_dpi.BR
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote

class ReleaseNotesAdapter() :
    ListAdapter<ReleaseNote, ReleaseNotesViewHolder>(ReleaseNotesDiffCallback) {

    // ViewHolderを作る場合に呼ばれる。ViewHolderの生成を書けばよい
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseNotesViewHolder {

        return ReleaseNotesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
        )
    }

    // ViewHolderに設定する値をバインディングする
    override fun onBindViewHolder(holder: ReleaseNotesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ビューの種類を返す。行によってビューの切り替えが必要な時だけ使えばよい。
    // DataBindingUtilを使用する場合には必要だが、ReleaseNotesViewBindingを使用するなら必要ない
    // コンストラクタに渡した配列などでViewTypeをpositionから返すようにするのがスタンダード
    // このメソッドの値を取得してonCreateViewHolderで違うレイアウトを返したり、onBindViewHolderで違う値を設定したりする
    override fun getItemViewType(position: Int): Int =
        R.layout.release_notes_view
}

// onBindViewHolderで表示する値をバインディングするのに使用する
class ReleaseNotesViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(releaseNote: ReleaseNote) {
        // DataBindingでBRが生成される。bindingに設定する値を指定する
        binding.setVariable(BR.releaseNote, releaseNote)
        // 保留中のbindingを即座に反映させる
        binding.executePendingBindings()
    }
}

// ListAdapterが追加・変更・削除を行った場合に、比較するのに使用される
object ReleaseNotesDiffCallback : DiffUtil.ItemCallback<ReleaseNote>() {
    // idとなるものを比較する。ここではどの値も重複する可能性があるため全部の項目で比較している
    override fun areItemsTheSame(oldItem: ReleaseNote, newItem: ReleaseNote): Boolean {
        return oldItem.appVersion == newItem.appVersion &&
            oldItem.date == newItem.date &&
            oldItem.description == newItem.description
    }

    // データの比較
    override fun areContentsTheSame(oldItem: ReleaseNote, newItem: ReleaseNote): Boolean =
        oldItem == newItem
}
