package com.example.breeze.ui.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breeze.R
import kotlinx.coroutines.flow.first


val Context.dataStore by preferencesDataStore(name = "onboarding")
class OnBoardingViewModel(private val context: Context) : ViewModel() {
    private val dataStoreKey = booleanPreferencesKey("finished")
    suspend fun saveOnBoardingFinished() {
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = true
        }
    }
    suspend fun isOnBoardingFinished(): Boolean {
        return context.dataStore.data.first()[dataStoreKey] ?: false
    }
}
class OnBoardingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnBoardingViewModel::class.java)) {
            return OnBoardingViewModel(context) as T
        }
        throw IllegalArgumentException(context.getString(R.string.unknown_viewmodel))
    }
}