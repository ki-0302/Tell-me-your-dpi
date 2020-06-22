package com.maho_ya.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.maho_ya.data.device.DataDeviceDataSource
import com.maho_ya.data.device.DataDeviceRepository
import com.maho_ya.domain.device.DeviceUseCase
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewModel: HomeVieModel
            by viewModels {
                HomeViewModelFactory(
                    DeviceUseCase(
                        DataDeviceRepository(
                            DataDeviceDataSource(
                                activity?.applicationContext
                            )
                        )
                    )
                )
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentHomeBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val fab: com.google.android.material.floatingactionbutton.FloatingActionButton
                = requireView().findViewById(R.id.fab)

        fab.setOnClickListener {

            copyDeviceInfoToClipboard()

            //fab.setImageResource(R.drawable.ic_check)
            //fab.setRippleColor(ContextCompat.getColor(this, R.color.colorPrimary))
            Snackbar.make(it, "デバイス情報をコピーしました。", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }

    }

    private fun copyDeviceInfoToClipboard() {

        val clipboardManager: ClipboardManager =
            this.activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipboardManager.setPrimaryClip(ClipData.newPlainText("", formatDeviceInfoForClipboard()))
    }

    private fun formatDeviceInfoForClipboard(): String {

        return getString(R.string.device_density_qualifier_title) + ": " +
                    viewModel.device.value?.densityQualifier + "\n" +
            getString(R.string.device_density_dpi_title) + ": " +
                    viewModel.device.value?.densityDpi.toString() + "\n" +
            getString(R.string.device_real_display_size_width_title) + ": " +
                    getString(R.string.device_real_display_size, viewModel.device.value?.realDisplaySizeWidth) + "\n" +
            getString(R.string.device_real_display_size_height_title) + ": " +
                    getString(R.string.device_real_display_size, viewModel.device.value?.realDisplaySizeHeight) + "\n" +
            getString(R.string.device_brand_title) + ": " +
                    viewModel.device.value?.brand + "\n" +
            getString(R.string.device_model_title) + ": " +
                    viewModel.device.value?.model + "\n" +
            getString(R.string.device_api_level_title) + ": " +
                    viewModel.device.value?.apiLevel.toString() + "\n" +
            getString(R.string.device_android_os_version_title) + ": " +
                    viewModel.device.value?.androidOsVersion + "\n" +
            getString(R.string.device_android_code_name_title) + ": " +
                    viewModel.device.value?.androidCodeName + "\n" +
            getString(R.string.device_memory_size_title) + ": " +
                    getString(R.string.device_memory_size,
                        viewModel.device.value?.totalMemory,
                        viewModel.device.value?.availableMemory)
    }


}

@BindingAdapter("deviceRealDisplaySize")
fun setDeviceRealDisplaySize(textView: TextView, deviceRealDisplaySize: Int) {

    textView.text = textView.resources.getString(
        R.string.device_real_display_size, deviceRealDisplaySize)
}

@BindingAdapter(value = ["deviceMemoryTotalSize", "deviceMemoryAvailableSize"])
fun setMemorySize(textView: TextView,
                  deviceMemoryTotalSize: Int,
                  deviceMemoryAvailableSize: Int) {

    textView.text = textView.resources.getString(
        R.string.device_memory_size, deviceMemoryTotalSize, deviceMemoryAvailableSize)
}