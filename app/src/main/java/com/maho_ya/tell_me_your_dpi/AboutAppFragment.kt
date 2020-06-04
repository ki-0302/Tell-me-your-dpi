package com.maho_ya.tell_me_your_dpi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        val mutableList = mutableListOf<MyData>()
        mutableList.add(MyData("Density qualifier", "dpi"))
        mutableList.add(MyData("Density DPI", "${resources.displayMetrics.densityDpi}"))

        val viewManager = LinearLayoutManager(activity)
        val viewAdapter = DpiInfoAdapter(mutableList)

        // requireView(): NonNullのonCreateViewで取得したViewを返す。エラーの場合はIllegalStateException
        requireView().findViewById<RecyclerView>(R.id.aboutAppMenuList).apply {
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