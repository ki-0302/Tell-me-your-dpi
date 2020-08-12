package com.maho_ya.data.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var appLaunchCount: Int
}

class SharedPreferenceStorage @Inject constructor(
    @ApplicationContext context: Context
) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "tdp"
        const val PREF_APP_LAUNCH_COUNT = "pref_app_launch_count"
    }

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, MODE_PRIVATE
        )
    }

    override var appLaunchCount: Int
        by IntPreference(prefs, PREF_APP_LAUNCH_COUNT, 0)
}

class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value) }
    }
}
