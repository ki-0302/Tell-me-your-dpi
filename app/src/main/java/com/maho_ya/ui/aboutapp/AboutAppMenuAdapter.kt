package com.maho_ya.ui.aboutapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.google.androidbrowserhelper.trusted.LauncherActivity
import com.maho_ya.tell_me_your_dpi.R

class AboutAppMenuAdapter(private val dataSet: MutableList<AboutAppMenuData>) :
    RecyclerView.Adapter<AboutAppMenuAdapter.AboutAppMenuViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class AboutAppMenuViewHolder(constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout) {

        private var container: ConstraintLayout? = null
        private var menuContent: TextView? = null

        init {
            container = itemView.findViewById(R.id.container)
            menuContent = itemView.findViewById(R.id.menuContent)
        }

        fun bind(text: AboutAppMenuData) {
            container?.setOnClickListener {

                when(text.key) {
                    2 -> {
                        val intent = Intent(it.context, LauncherActivity::class.java)
                        it.context.startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(it.context, OssLicensesMenuActivity::class.java)
                        intent.putExtra("title", "オープンソース ライセンス")
                        it.context.startActivity(intent)
                    }
                }
                Snackbar.make(it, "クリックしました。", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }

            menuContent?.text = text.menuContent
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AboutAppMenuViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.about_app_view, parent, false) as ConstraintLayout
        // set the view's size, margins, paddings and layout parameters

        return AboutAppMenuViewHolder(
            textView
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AboutAppMenuViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}