package com.thitipat.template.room

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "tblColor")
data class ColorData(
    @PrimaryKey(autoGenerate = true)
    val seq: Int,
    val colorGroup: String,
    val colorCode: String,
) : Parcelable

@Parcelize
@Entity(tableName = "tblItem")
data class ItemData(
    @PrimaryKey(autoGenerate = true)
    val seq: Int,
    val location: String,
    val partNumber: String,
    val partCode: String,
    val partDesc: String,
    val dockCode: String,
    val iIdentifyNo: String = "",
    val printingQty: Int = 0,
    val scan1: String = "",
    val scan2: String = "",
    val showPhotoSpec: Boolean = false,
    val scan3: String = "",
    val scan4: String = "",
    val printSticker: Boolean = false,
) : Parcelable

@Parcelize
@Entity(tableName = "tblLog")
data class LogData(
    @PrimaryKey(autoGenerate = true)
    val seq: Int,
    val scanDate: String,
    val partNumber: String,
    val partCode: String,
    val emergencyTagData: String,
    val scan1: String,
    val scan2: String,
    val scan3: String,
    val scan4: String,
    val scanResult: String,
    val reason: String,
    val printingQty: Int = 0,
) : Parcelable