package com.thitipat.template.modules.offline.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thitipat.template.data.ResponseOfflineResult
import com.thitipat.template.room.LogData
import com.thitipat.template.utils.Shared
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanEmergencyViewModel(private val repository: ScanEmergencyRepository) : ViewModel() {

    val logData = repository.logData
    val scan0Result = MutableLiveData<ResponseOfflineResult>()
    val scan1Result = MutableLiveData<ResponseOfflineResult>()
    val scan2Result = MutableLiveData<ResponseOfflineResult>()
    val scan3Result = MutableLiveData<ResponseOfflineResult>()
    val scan4Result = MutableLiveData<ResponseOfflineResult>()
    val confirmScan = MutableLiveData<ResponseOfflineResult>()

    var emergency = ""

    var partNo = ""
    val KBPartNo = MutableLiveData<String>()
    val KBPrintQty = MutableLiveData<Int>()
    var partCode = ""
    var orderNo = ""
    var arrivalDateTime = ""
    var qty = ""

    var tagPartNo = ""
    var tagRunning = ""

    fun scan0(barcodeEmergency: String) {
        clearData()
        if (!checkDigit(Shared.BARCODE.EMERGENCY, barcodeEmergency)) {
            scan0Result.postValue(ResponseOfflineResult(false, "Barcode emergency is not correct!"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            scan0Result.postValue(ResponseOfflineResult(true, isPartExists = repository.scan0(emergency)))
        }
    }

    fun scan1(barcodeKanban: String) {
        if (!checkDigit(Shared.BARCODE.KANBAN, barcodeKanban)) {
            scan1Result.postValue(ResponseOfflineResult(false, "Barcode kanban is not correct!"))
            return
        }
        if (!replaceDash(barcodeKanban).contains(replaceDash(emergency))) {
            scan1Result.postValue(ResponseOfflineResult(false, "Barcode kanban is not contain emergency tag!"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            scan1Result.postValue(ResponseOfflineResult(true, itemData = repository.scan1(partNo, partCode)))
        }
    }

    fun scan2(barcodeTag: String) {
        if (!checkDigit(Shared.BARCODE.TAG, barcodeTag)) {
            scan2Result.postValue(ResponseOfflineResult(false, "Barcode tag is not correct!"))
            return
        }

        /*if (
            (replaceDash(barcodeTag).contains(replaceDash(KBPartNo.value.toString())) || replaceDash(KBPartNo.value.toString()).contains(replaceDash(barcodeTag)))
            || (replaceDash(barcodeTag).contains(subStr(replaceDash(KBPartNo.value.toString()), 1, 10)) || subStr(replaceDash(KBPartNo.value.toString()), 1, 10).contains(replaceDash(barcodeTag)))
            || (replaceDash(KBPartNo.value.toString()).contains(subStr(replaceDash(barcodeTag), 2, 10)) || subStr(replaceDash(barcodeTag), 2, 10).contains(replaceDash(KBPartNo.value.toString())))
        ) {
            scan2Result.postValue(ResponseOfflineResult(true))
        } else {
            scan2Result.postValue(ResponseOfflineResult(false, "Barcode tag is not found!"))
        }*/

        viewModelScope.launch(Dispatchers.IO) {
            scan2Result.postValue(ResponseOfflineResult(true, isPartExists = repository.scan2(partNo, partCode,tagPartNo)))
        }
    }

    fun scan3() {
        scan3Result.postValue(ResponseOfflineResult(true))
    }

    fun scan4() {
        scan4Result.postValue(ResponseOfflineResult(true))
    }

    fun confirmScan(barcodeEmergency: String, barcodeKanban: String, barcodeTag: String, barcode3: String? = "", barcode4: String? = "") {
        /*val logData = LogData(
            0,
            "${Shared.getDateNow()} ${Shared.getTimeNow()}",
            KBPartNo.value.toString(),
            partCode,
            barcodeEmergency,
            barcodeKanban,
            barcodeTag,
            barcode3 ?: "",
            barcode4 ?: "",
            "OK",
            "",
            KBPrintQty.value ?: 1
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.confirmScan(logData)
        }*/

        confirmScan.postValue(ResponseOfflineResult(true))
    }

    private fun checkDigit(scanFunc: Shared.BARCODE, barcode: String): Boolean {
        when (scanFunc) {
            Shared.BARCODE.EMERGENCY -> {
                return when (barcode.length) {
                    21 -> {
                        emergency = subStr(barcode, 1, 14)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            Shared.BARCODE.KANBAN -> {
                return when (barcode.length) {
                    83 -> {
                        partNo = subStr(barcode, 16, 12)
                        partCode = subStr(barcode, 69, 4)
                        orderNo = subStr(barcode, 4, 10)
                        arrivalDateTime = subStr(barcode, 52, 15)
                        qty = subStr(barcode, 37, 7)
                        true
                    }
                    /*158 -> {
                        partNo = subStr(barcode, 20, 12)
                        partCode = subStr(barcode, 32, 4)
                        orderNo = subStr(barcode, 4, 10)
                        arrivalDateTime = subStr(barcode, 49, 12)
                        qty = subStr(barcode, 41, 3)
                        true
                    }*/
                    166 -> {
                        partNo = subStr(barcode, 28, 12)
                        partCode = subStr(barcode, 40, 4)
                        orderNo = subStr(barcode, 12, 12)
                        arrivalDateTime = subStr(barcode, 57, 8)
                        qty = subStr(barcode, 49, 7)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            Shared.BARCODE.TAG -> {
                val sub = barcode.split(";")
                if (sub.size != 5) {
                    if (barcode.length == 11 || barcode.length == 13) {
                        tagPartNo = barcode
                        return true
                    }
                    return false
                } else {
                    tagPartNo = sub[1]
                    return true
                }
            }
        }
    }

    private fun subStr(str: String, start: Int, digit: Int): String {
        return Shared.replaceStr(str.substring(Shared.setStart(start), Shared.setDigit(Shared.setStart(start), digit)))
    }

    private fun replaceDash(value: String): String {
        return value.replace("-", "")
    }

    private fun clearData() {
        emergency = ""

        KBPartNo.postValue("")
        partNo = ""
        partCode = ""
        orderNo = ""
        arrivalDateTime = ""
        qty = ""
        KBPrintQty.postValue(0)

        tagPartNo = ""
        tagRunning = ""
    }
}