package com.maho_ya.tell_me_your_dpi.data.device

import android.app.ActivityManager
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.maho_ya.tell_me_your_dpi.model.Device
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import javax.inject.Inject

interface DeviceDataSource {
    suspend fun getDevice(): Device
}

class DataDeviceDataSource (
    private val context: Context?
) : DeviceDataSource {

    override suspend fun getDevice(): Device {
        val displayRealSize = getDisplayRealSize()
        val memoryInfo = getMemoryInfo()

        return Device(
            densityQualifier = getDensityQualifier(),
            densityDpi = getDensityDpi(),
            realDisplaySizeWidth = displayRealSize.x,
            realDisplaySizeHeight = displayRealSize.y,
            brand = Build.BRAND,
            model = Build.MODEL,
            apiLevel = Build.VERSION.SDK_INT,
            androidOsVersion = Build.VERSION.RELEASE,
            androidCodeName = getAndroidCodeName(),
            totalMemory = convertMemorySizeToMB(memoryInfo?.totalMem),
            availableMemory = convertMemorySizeToMB(memoryInfo?.availMem)
        )
    }

    private fun getDensityQualifier(): String {
        if (context == null) return "Unknown"

        return try {

            when (context.resources.displayMetrics.densityDpi) {
                0 -> "UnDefined"
                in 1..120 -> "ldpi"
                in 121..160 -> "mdpi"
                in 161..240 -> "hdpi"
                in 241..320 -> "xhdpi"
                in 321..480 -> "xxhdpi"
                in 481..640 -> "xxxhdpi"
                else -> context.resources.displayMetrics.densityDpi.toString() + "dpi"
            }
        } catch (e: Exception) {
            Timber.d(e)
            "Unknown"
        }
    }

    private fun getDensityDpi(): Int {
        return try {
            context?.resources?.displayMetrics?.densityDpi ?: 0
        } catch (e: Exception) {
            Timber.d(e)
            0
        }
    }

    private fun getDisplayRealSize(): Point {
        return when {
            context == null -> Point()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                getDisplayRealSizeForROrHigher()
            }
            else -> getDisplayRealSizeForLessThanR()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getDisplayRealSizeForROrHigher(): Point {
        val bounds = getWindowManager().currentWindowMetrics.bounds
        return Point(bounds.width(), bounds.height())
    }

    @Suppress("DEPRECATION")
    private fun getDisplayRealSizeForLessThanR(): Point {
        return try {
            val realSize = Point()
            getDisplay()?.getRealSize(realSize)
            realSize
        } catch (e: Exception) {
            Point()
        }
    }

    private fun getDisplay(): Display? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> context?.display
            else -> getDisplayForLessThanR()
        }
    }

    @Suppress("DEPRECATION")
    private fun getDisplayForLessThanR(): Display? {
        return getWindowManager().defaultDisplay
    }

    private fun getAndroidCodeName(): String {

        // https://source.android.google.cn/setup/start/build-numbers?hl=en
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.BASE, Build.VERSION_CODES.BASE_1_1 -> "Base"
            Build.VERSION_CODES.CUPCAKE -> "Cupcake"
            Build.VERSION_CODES.DONUT -> "Donut"
            Build.VERSION_CODES.ECLAIR,
            Build.VERSION_CODES.ECLAIR_0_1,
            Build.VERSION_CODES.ECLAIR_MR1 -> "Eclair"
            Build.VERSION_CODES.FROYO -> "Froyo"
            Build.VERSION_CODES.GINGERBREAD,
            Build.VERSION_CODES.GINGERBREAD_MR1 -> "Gingerbread"
            Build.VERSION_CODES.HONEYCOMB,
            Build.VERSION_CODES.HONEYCOMB_MR1,
            Build.VERSION_CODES.HONEYCOMB_MR2 -> "Honeycomb"
            Build.VERSION_CODES.ICE_CREAM_SANDWICH,
            Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 -> "Ice Cream Sandwich"
            Build.VERSION_CODES.JELLY_BEAN,
            Build.VERSION_CODES.JELLY_BEAN_MR1,
            Build.VERSION_CODES.JELLY_BEAN_MR2 -> "Jelly Bean"
            Build.VERSION_CODES.KITKAT,
            Build.VERSION_CODES.KITKAT_WATCH -> "KitKat/4.4W"
            Build.VERSION_CODES.LOLLIPOP,
            Build.VERSION_CODES.LOLLIPOP_MR1 -> "Lollipop"
            Build.VERSION_CODES.M -> "Marshmallow"
            Build.VERSION_CODES.N,
            Build.VERSION_CODES.N_MR1 -> "Nougat"
            Build.VERSION_CODES.O,
            Build.VERSION_CODES.O_MR1 -> "Oreo"
            Build.VERSION_CODES.P -> "Pie"
            Build.VERSION_CODES.Q -> "Android10 (Quince Tart)"
            Build.VERSION_CODES.R -> "Android11 (Red Velvet Cake)"
            Build.VERSION_CODES.S -> "Android12 (Snow Cone)"
            Build.VERSION_CODES.S_V2 -> "Android12L (Sv2)"
            33 -> "Android13 (Tiramisu)"
            else -> "UnKnown"
        }
    }

    private fun convertMemorySizeToMB(memorySize: Long?): Int {

        if (memorySize == null || memorySize <= 0) {
            return 0
        }

        return (memorySize / 1024 / 1024).toInt()
    }

    // Get a MemoryInfo object for the device's current memory status.
    private fun getMemoryInfo(): ActivityManager.MemoryInfo? {

        return try {
            val activityManager = context
                ?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            ActivityManager.MemoryInfo().also { memoryInfo ->
                activityManager?.getMemoryInfo(memoryInfo)
            }
        } catch (e: Exception) {
            Timber.d(e)
            null
        }
    }

    private fun getWindowManager(): WindowManager {
        return context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}
