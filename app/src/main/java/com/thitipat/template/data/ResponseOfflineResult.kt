package com.thitipat.template.data

import com.thitipat.template.room.ItemData
import com.thitipat.template.room.LogData

data class ResponseOfflineResult(
    val isSuccess: Boolean,
    val isMessage: String? = null,
    val itemData: ItemData? = null,
    val logData: LogData? = null,
    val isPartExists: Int? = 0,
)
