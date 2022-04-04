package com.maho_ya.tell_me_your_dpi.ui.aboutapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentAboutAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    private val aboutAppViewModel: AboutAppViewModel by viewModels()
    private val privacyPolicyUrl = "https://maho-ya.firebaseapp.com//privacy.html"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAboutAppBinding.bind(view)
        binding.viewModel = aboutAppViewModel
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
            val color = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> getColorForMOrHigher()
                else -> getColorForLessThanM()
            }
            val defaultColors = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(color)
                .setNavigationBarColor(color)
                .build()
            val builder = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(defaultColors)
                .build()
            builder.launchUrl(activity, Uri.parse(privacyPolicyUrl))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getColorForMOrHigher() =
        resources.getColor(R.color.color_primary, context?.theme)

    @Suppress("DEPRECATION")
    private fun getColorForLessThanM()  =
        resources.getColor(R.color.color_primary)

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
        R.string.about_version_title, versionName
    )
}
