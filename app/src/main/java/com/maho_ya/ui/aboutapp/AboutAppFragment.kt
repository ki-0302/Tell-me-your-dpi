package com.maho_ya.ui.aboutapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.androidbrowserhelper.trusted.LauncherActivity
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    private val viewModel: AboutAppViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAboutAppBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        view.findViewById<TextView>(R.id.privacy_policy)?.apply {
            setOnClickListener { openPrivacyPolicySite() }
        }

        view.findViewById<TextView>(R.id.oss_licences)?.apply {
            setOnClickListener { openOssLicences() }
        }
    }

    private fun openPrivacyPolicySite() {

        activity?.let { activity ->
            activity.startActivity(
                Intent(activity, LauncherActivity::class.java)
            )
        }
    }

    private fun openOssLicences() {

        activity?.let { activity ->

            val intent = Intent(activity, OssLicensesMenuActivity::class.java)
            intent.putExtra("title", getString(R.string.about_oss_licences_title))
            startActivity(intent)
        }
    }
}

@BindingAdapter("versionName")
fun setAppVersion(textView: TextView, versionName: String) {

    textView.text = textView.resources.getString(
        R.string.about_version_title, versionName)
}