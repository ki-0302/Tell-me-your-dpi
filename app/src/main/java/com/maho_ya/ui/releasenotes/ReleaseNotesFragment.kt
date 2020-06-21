package com.maho_ya.ui.releasenotes

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.maho_ya.data.releasenotes.DataReleaseNotesDataSource
import com.maho_ya.data.releasenotes.DataReleaseNotesRepository
import com.maho_ya.domain.releasenotes.ReleaseNotesUseCase
import com.maho_ya.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentReleaseNotesBinding

class ReleaseNotesFragment: Fragment(R.layout.fragment_release_notes) {

    private val viewModel: ReleaseNotesViewModel
            by viewModels {
                ReleaseNotesViewModelFactory(
                    ReleaseNotesUseCase(
                    DataReleaseNotesRepository(
                        DataReleaseNotesDataSource()
                    )))
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentReleaseNotesBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}

@BindingAdapter(value = ["releaseNotes", "hasError"])
fun setReleaseNotesItems(recyclerView: RecyclerView, releaseNotes: List<ReleaseNote>?, hasError: Boolean) {

    releaseNotes ?: return

    if (hasError) return

    if (recyclerView.adapter == null) {
        recyclerView.adapter = ReleaseNotesAdapter()
    }

    // SubmitList method of ListAdapter submit a new list to be diffed, and displayed.
    (recyclerView.adapter as ReleaseNotesAdapter).submitList(releaseNotes)

    recyclerView.apply {
        // Add separate line.
        addItemDecoration(
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        )

        setHasFixedSize(true)
    }
}