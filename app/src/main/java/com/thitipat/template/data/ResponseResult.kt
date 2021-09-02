package com.thitipat.template.data

import com.thitipat.template.room.ItemData

data class ResponseResult(
    val isSuccess: Boolean,
    val isMessage: String? = null,
    val kanbanData: KanbanData? = null,
    val masterItem: List<ItemData>? = null,
    val isItemData: ItemData? = null,
)

data class KanbanData(
    val partNumber: String,
    val scan1: String,
    val scan2: String,
    val scan3: String,
    val scan4: String
)
