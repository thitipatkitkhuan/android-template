package com.thitipat.template.modules.online.viewmodel

import android.content.Context
import com.thitipat.template.data.ResponseResult
import com.thitipat.template.services.Query

class ScanRepository(private val context: Context) {

    private val query = Query(context)

    suspend fun scan1(barcodeKanban: String): ResponseResult {
        return query.scan1(barcodeKanban)
    }

    suspend fun scan2(barcodeKanban: String, barcodeTag: String): ResponseResult {
        return query.scan2(barcodeKanban, barcodeTag)
    }

    suspend fun confirmScan(running: String): ResponseResult {
        return query.confirmScan(running)
    }

}
