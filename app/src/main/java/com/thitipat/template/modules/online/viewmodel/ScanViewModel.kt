package com.thitipat.template.modules.online.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thitipat.template.data.ResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanViewModel(private val repository: ScanRepository) : ViewModel() {

    val scan1Result = MutableLiveData<ResponseResult>()
    val scan2Result = MutableLiveData<ResponseResult>()
    val confirmScan = MutableLiveData<ResponseResult>()

    fun scan1(barcodeScan1: String) {
        viewModelScope.launch(Dispatchers.IO) {
            scan1Result.postValue(repository.scan1(barcodeScan1))
        }
    }

    fun scan2(barcodeScan1: String, barcodeScan2: String) {
        viewModelScope.launch(Dispatchers.IO) {
            scan2Result.postValue(repository.scan2(barcodeScan1, barcodeScan2))
        }
    }

    fun confirmScan(running: String) {
        viewModelScope.launch(Dispatchers.IO) {
            confirmScan.postValue(repository.confirmScan(running))
        }
    }
}