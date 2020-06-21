package com.maho_ya.ui.aboutapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.androidbrowserhelper.trusted.LauncherActivity
import com.maho_ya.model.AboutApp
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.model.AboutAppKey.PRIVACY
import com.maho_ya.model.AboutAppKey.LICENSES

class AboutAppMenuAdapter(private val dataSet: MutableList<AboutApp>) :
    RecyclerView.Adapter<AboutAppMenuAdapter.AboutAppMenuViewHolder>() {

    class AboutAppMenuViewHolder(constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout) {

        private var container: ConstraintLayout? = null
        private var menuContent: TextView? = null

        init {
            container = itemView.findViewById(R.id.container)
            menuContent = itemView.findViewById(R.id.menuContent)
        }

        fun bind(text: AboutApp) {
            container?.setOnClickListener {

                when(text.key) {
                    PRIVACY -> {
                        val intent = Intent(it.context, LauncherActivity::class.java)
                        it.context.startActivity(intent)
                    }
                    LICENSES -> {
                        val intent = Intent(it.context, OssLicensesMenuActivity::class.java)
                        intent.putExtra("title", "オープンソース ライセンス")
                        it.context.startActivity(intent)
                    }
                }
            }

            menuContent?.text = text.menuContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AboutAppMenuViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.about_app_view, parent, false) as ConstraintLayout

        return AboutAppMenuViewHolder(
            textView
        )
    }

    override fun onBindViewHolder(holder: AboutAppMenuViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}