package com.maho_ya.di

import android.content.Context
import com.maho_ya.MainApplication
import dagger.Module
import dagger.Provides

@Module
internal class AppModule(@get:Provides val applicationContext: Context)