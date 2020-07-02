package com.maho_ya.data.device

import android.app.ActivityManager
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import com.maho_ya.model.Device
import java.lang.Exception
import javax.inject.Inject
import timber.log.Timber

interface DeviceDataSource {
    suspend fun getDevice(): Device
}

class DataDeviceDataSource @Inject constructor(
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

        if (context == null) return Point()

        return try {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val defaultDisplay = wm.defaultDisplay
            val realSize = Point()
            defaultDisplay.getRealSize(realSize)
            realSize
        } catch (e: Exception) {
            Point()
        }
    }

    private fun getAndroidCodeName(): String {

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
            Build.VERSION_CODES.Q -> "Android 10 (Q)"
            30 -> "Android 11 (R)"
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
}
