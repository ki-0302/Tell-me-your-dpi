package com.maho_ya.ui.aboutapp

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maho_ya.tell_me_your_dpi.R


class AboutAppFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val packageInfo: PackageInfo =
            activity?.applicationContext?.packageManager!!.getPackageInfo(
                activity?.applicationContext!!.packageName, 0)
        val versionName = packageInfo.versionName

        val mutableList = mutableListOf<AboutAppMenuData>()
        mutableList.add(
            AboutAppMenuData(
                1,
                "バージョン $versionName"
            )
        )
        mutableList.add(
            AboutAppMenuData(
                2,
                "プライバシーポリシー"
            )
        )
        mutableList.add(
            AboutAppMenuData(
                3,
                "オープンソース ライセンス"
            )
        )

        val viewManager = LinearLayoutManager(activity)
        val viewAdapter =
            AboutAppMenuAdapter(mutableList)



        // requireView(): NonNullのonCreateViewで取得したViewを返す。エラーの場合はIllegalStateException
        requireView().findViewById<RecyclerView>(R.id.aboutAppMenuList).apply {

            val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }


    }


}