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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseNotesViewHolder {

        return ReleaseNotesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReleaseNotesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int =
        R.layout.release_notes_view
}

class ReleaseNotesViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(releaseNote: ReleaseNote) {
        // DataBinding generate BR.
        binding.setVariable(BR.releaseNote, releaseNote)
        binding.executePendingBindings()
    }
}

object ReleaseNotesDiffCallback : DiffUtil.ItemCallback<ReleaseNote>() {
    override fun areItemsTheSame(oldItem: ReleaseNote, newItem: ReleaseNote): Boolean {

        return oldItem.appVersion == newItem.appVersion &&
            oldItem.date == newItem.date &&
            oldItem.description == newItem.description
    }

    override fun areContentsTheSame(oldItem: ReleaseNote, newItem: ReleaseNote): Boolean =
        oldItem == newItem
}
