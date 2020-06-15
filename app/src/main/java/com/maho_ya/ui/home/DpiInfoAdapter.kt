package com.maho_ya.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.maho_ya.tell_me_your_dpi.R

class DpiInfoAdapter(private val dataSet: MutableList<MyData>) :
    RecyclerView.Adapter<DpiInfoAdapter.DpiInfoViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class DpiInfoViewHolder(constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout) {

        private var container: ConstraintLayout? = null
        private var title: TextView? = null
        private var content: TextView? = null


        init {
            container = itemView.findViewById(R.id.container)
            title = itemView.findViewById(R.id.textView2)
            content = itemView.findViewById(R.id.textView3)
        }

        fun bind(text: MyData) {
            container?.setOnClickListener {
//                val action =
//                    MainFragmentDirections
//                        .actionMainFragmentToAboutAppFragment()
//                it.findNavController().navigate(R.id.action_aboutAppFragment_to_privacyFragment)

                Snackbar.make(it, "クリックしました。", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }
            title?.text = text.key
            content?.text = text.value
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DpiInfoViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_text_view, parent, false) as ConstraintLayout
        // set the view's size, margins, paddings and layout parameters



        return DpiInfoViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DpiInfoViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(dataSet.get(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}