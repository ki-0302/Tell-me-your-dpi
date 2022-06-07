package com.maho_ya.tell_me_your_dpi.ui.aboutapp

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentAboutAppBinding
import com.maho_ya.tell_me_your_dpi.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

// https://developer.android.com/guide/fragments/create#create
@AndroidEntryPoint
class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#create
        val binding = FragmentAboutAppBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.composeView.apply {
            // Lifecycleが破棄された時にCompositionを破棄する設定
            // https://developer.android.google.cn/jetpack/compose/interop/interop-apis?hl=ja#composition-strategy
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    AboutAppScreen()
                }
            }
        }
    }
}
