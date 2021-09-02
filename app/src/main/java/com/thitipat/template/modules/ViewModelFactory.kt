package com.thitipat.template.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thitipat.template.modules.offline.viewmodel.ScanEmergencyRepository
import com.thitipat.template.modules.offline.viewmodel.ScanEmergencyViewModel
import com.thitipat.template.modules.online.viewmodel.ScanRepository
import com.thitipat.template.modules.online.viewmodel.ScanViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repository: Any?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(repository as ScanRepository) as T
        }
        if (modelClass.isAssignableFrom(ScanEmergencyViewModel::class.java)) {
            return ScanEmergencyViewModel(repository as ScanEmergencyRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}