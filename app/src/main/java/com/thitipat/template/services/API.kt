package com.thitipat.template.services

import com.thitipat.template.data.ResponseResult
import com.thitipat.template.room.LogData

interface API {

    suspend fun scan1(barcodeKanban: String): ResponseResult
    suspend fun scan2(barcodeKanban: String, barcodeTag: String): ResponseResult
    suspend fun confirmScan(running: String): ResponseResult
    suspend fun downloadMasterItem(): ResponseResult
    suspend fun downloadMasterColor(): ResponseResult
    suspend fun uploadLogData(logData: LogData): ResponseResult
}