package com.thitipat.template.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MainDao {
    @Query("DELETE FROM tblItem")
    suspend fun dropTblItem()

    @Query("DELETE FROM tblColor")
    suspend fun dropTblColor()

    @Query("DELETE FROM tblLog")
    suspend fun dropTblLog()

    @Query("SELECT * FROM tblItem")
    fun getItemData(): LiveData<List<ItemData>>

    @Query("SELECT * FROM tblColor")
    fun getColorData(): LiveData<List<ColorData>>

    @Query("SELECT * FROM tblLog")
    fun getLogData(): LiveData<List<LogData>>

    @Query("SELECT * FROM tblLog")
    fun getLog(): List<LogData>

    @Insert
    suspend fun insertMasterItem(itemData: ItemData)

    @Insert
    suspend fun insertMasterColor(colorData: ColorData)

    @Query("SELECT COUNT(*) FROM tblItem WHERE `replace`(partNumber,'-','') = `replace`(:partNo,'-','') LIMIT 1")
    fun scan0(partNo: String): Int

    @Query("SELECT * FROM tblItem WHERE `replace`(partNumber,'-','') = `replace`(:partNo,'-','') AND `replace`(partCode,'-','') = `replace`(:partCode,'-','') LIMIT 1")
    fun scan1(partNo: String, partCode: String): ItemData

    @Query("SELECT COUNT(*) FROM tblItem WHERE (`replace`(:partNo,'-','') LIKE '%' || `replace`(partNumber,'-','') || '%' AND `replace`(:partCode,'-','') LIKE '%' || `replace`(:partCode,'-','') || '%') AND ( ( ( `substr`(`replace`(partNumber,'-',''), 1, 10) LIKE '%' || `replace`(:tagPartNo,'-','') || '%') OR (`replace`(:tagPartNo,'-','') LIKE '%' ||  `substr`(`replace`(partNumber,'-',''), 1, 10) || '%') ) OR ( (`replace`(partNumber,'-','') LIKE '%' ||  `substr`(`replace`(:tagPartNo,'-',''), 2, 10)  || '%') OR ( `substr`(`replace`(:tagPartNo,'-',''), 1, 10) LIKE '%' || `replace`(partNumber,'-','') || '%') ) OR ( ( `substr`(`replace`(partNumber,'-',''), 1, 10) LIKE '%' ||  `substr`(`replace`(:tagPartNo,'-',''), 2, 10)  || '%') OR ( `substr`(`replace`(:tagPartNo,'-',''), 2, 10) LIKE '%' ||  `substr`(`replace`(partNumber,'-',''), 1, 10) || '%') ) ) LIMIT 1")
    fun scan2(partNo: String, partCode: String, tagPartNo: String): Int

    @Insert
    suspend fun confirmScan(logData: LogData)

    @Query("SELECT colorCode FROM tblColor WHERE colorGroup = :colorGroup AND colorCode IN (:colorCode) LIMIT 1")
    fun checkColorCorrect(colorGroup: String, colorCode: List<String>): String
}