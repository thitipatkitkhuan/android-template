package com.thitipat.template.modules.offline.viewmodel

import com.thitipat.template.room.ItemData
import com.thitipat.template.room.LogData
import com.thitipat.template.room.MainDao

class ScanEmergencyRepository(private val dao: MainDao) {

    val logData = dao.getLogData()

    fun scan0(partNo: String): Int {
        return dao.scan0(partNo)
    }

    fun scan1(partNo: String, partCode: String): ItemData {
        return dao.scan1(partNo, partCode)
    }

    fun scan2(partNo: String, partCode: String, tagPartNo: String): Int {
        return dao.scan2(partNo, partCode,tagPartNo)
    }

    suspend fun confirmScan(logData: LogData) {
        return dao.confirmScan(logData)
    }
}
