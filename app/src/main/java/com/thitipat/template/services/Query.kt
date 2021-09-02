package com.thitipat.template.services

import android.content.Context
import android.util.Log
import com.thitipat.template.R
import com.thitipat.template.data.KanbanData
import com.thitipat.template.data.ResponseResult
import com.thitipat.template.room.ItemData
import com.thitipat.template.room.LogData
import com.thitipat.template.utils.Shared
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class Query(private val context: Context) : API {

    override suspend fun scan1(barcodeKanban: String): ResponseResult {
        val pref = Shared.getSettingPrefs(context)
        var connection: Connection? = null
        var resultSet: ResultSet? = null
        try {
            connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)

            val parameters = ArrayList<ParameterResult>()
            parameters.add(ParameterResult("BarcodeKanban", barcodeKanban))

            val ps = ConnectionClass.setConnection(connection!!, "HT_SCAN_KANBAN", parameters)
            resultSet = ps.executeQuery()
            return if (resultSet.next()) {
                val isSuccess = resultSet.getBoolean(context.getString(R.string.txt_is_success))
                if (isSuccess) {
                    val partNumber = resultSet.getString("PartNumber")
                    val scan1 = resultSet.getString("Scan1")
                    val scan2 = resultSet.getString("Scan2")
                    val scan3 = resultSet.getString("Scan3") ?: ""
                    val scan4 = resultSet.getString("Scan4") ?: ""
                    ResponseResult(true, kanbanData = KanbanData(partNumber, scan1, scan2, scan3, scan4))
                } else {
                    val isMessage = resultSet.getString(context.getString(R.string.txt_is_message))
                    ResponseResult(false, isMessage)
                }
            } else {
                ResponseResult(false, context.getString(R.string.txt_false_result))
            }
        } catch (e: SQLException) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } catch (e: Exception) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    override suspend fun scan2(barcodeKanban: String, barcodeTag: String): ResponseResult {
        val pref = Shared.getSettingPrefs(context)
        var connection: Connection? = null
        var resultSet: ResultSet? = null
        try {
            connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)

            val parameters = ArrayList<ParameterResult>()
            parameters.add(ParameterResult("BarcodeKanban", barcodeKanban))
            parameters.add(ParameterResult("BarcodeTag", barcodeTag))

            val ps = ConnectionClass.setConnection(connection!!, "HT_SCAN_TAG", parameters)
            resultSet = ps.executeQuery()
            return if (resultSet.next()) {
                val isSuccess = resultSet.getBoolean(context.getString(R.string.txt_is_success))
                if (isSuccess) {
                    val running = resultSet.getString("Running") ?: ""
                    ResponseResult(true, "")
                } else {
                    val isMessage = resultSet.getString(context.getString(R.string.txt_is_message))
                    ResponseResult(false, isMessage)
                }
            } else {
                ResponseResult(false, context.getString(R.string.txt_false_result))
            }
        } catch (e: SQLException) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } catch (e: Exception) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    override suspend fun confirmScan(running: String): ResponseResult {
        val pref = Shared.getSettingPrefs(context)
        var connection: Connection? = null
        var resultSet: ResultSet? = null
        try {
            connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)

            val parameters = ArrayList<ParameterResult>()
            parameters.add(ParameterResult("Running", running))

            val ps = ConnectionClass.setConnection(connection!!, "HT_CONFIRM_SCAN", parameters)
            resultSet = ps.executeQuery()
            return if (resultSet.next()) {
                val isSuccess = resultSet.getBoolean(context.getString(R.string.txt_is_success))
                if (isSuccess) {
                    ResponseResult(true)
                } else {
                    val isMessage = resultSet.getString(context.getString(R.string.txt_is_message))
                    ResponseResult(false, isMessage)
                }
            } else {
                ResponseResult(false, context.getString(R.string.txt_false_result))
            }
        } catch (e: SQLException) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } catch (e: Exception) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    override suspend fun downloadMasterItem(): ResponseResult {
        val pref = Shared.getSettingPrefs(context)
        var connection: Connection? = null
        var resultSet: ResultSet? = null
        try {
            connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)

            val ps = ConnectionClass.setConnection(connection!!, "HT_DOWNLOAD_MASTER_ITEM", null)

            resultSet = ps.executeQuery()
            val arr = ArrayList<ItemData>()
            if (resultSet != null) {
                while (resultSet.next()) {

                    val seq = resultSet.getInt("Seq")
                    val location = resultSet.getString("Location")
                    val partNumber = resultSet.getString("PartNumber")
                    val partCode = resultSet.getString("PartCode")
                    val partDesc = resultSet.getString("PartDesc") ?: ""
                    val dockCode = resultSet.getString("DockCode") ?: ""
                    val identifyNo = resultSet.getString("IdentifyNo") ?: ""
                    val printingQty = resultSet.getInt("PrintingQty") ?: 0
                    val scan1 = resultSet.getString("Scan1") ?: ""
                    val scan2 = resultSet.getString("Scan2") ?: ""
                    val showPhotoSpec = resultSet.getBoolean("ShowPhotoSpec")
                    val scan3 = resultSet.getString("Scan3") ?: ""
                    val scan4 = resultSet.getString("Scan4") ?: ""
                    val printSticker = resultSet.getBoolean("PrintSticker")

                    //arr.add(ItemData(seq, location, partNumber, partCode, partDesc, dockCode, identifyNo, printingQty, scan1, scan2, showPhotoSpec, scan3, scan4, printSticker))
                }
                return ResponseResult(true, masterItem = arr)
            } else {
                return ResponseResult(false, "Not found!")
            }
        } catch (e: SQLException) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } catch (e: Exception) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    override suspend fun downloadMasterColor(): ResponseResult {
        val pref = Shared.getSettingPrefs(context)
        var connection: Connection? = null
        var resultSet: ResultSet? = null
        try {
            connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)

            val ps = ConnectionClass.setConnection(connection!!, "HT_DOWNLOAD_MASTER_COLOR", null)
            resultSet = ps.executeQuery()
            //val arr = ArrayList<ColorData>()
            if (resultSet != null) {
                while (resultSet.next()) {
                    val seq = resultSet.getInt("Seq")
                    val colorGroup = resultSet.getString("ColorGroup")
                    val colorCode = resultSet.getString("ColorCode")
                    //arr.add(ColorData(seq, colorGroup, colorCode))
                }
                return ResponseResult(true, "")
            } else {
                return ResponseResult(false, "Not found!")
            }
        } catch (e: SQLException) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } catch (e: Exception) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    override suspend fun uploadLogData(logData: LogData): ResponseResult {
        val pref = Shared.getSettingPrefs(context)
        var connection: Connection? = null
        var resultSet: ResultSet? = null
        try {
            connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)

            val parameters = ArrayList<ParameterResult>()
            /*parameters.add(ParameterResult("ScanDate", logData.scanDate))
            parameters.add(ParameterResult("PartNo", logData.partNumber))
            parameters.add(ParameterResult("PartCode", logData.partCode))
            parameters.add(ParameterResult("EmergencyTagData", logData.emergencyTagData))
            parameters.add(ParameterResult("Scan1", logData.scan1))
            parameters.add(ParameterResult("Scan2", logData.scan2))
            parameters.add(ParameterResult("Scan3", logData.scan3))
            parameters.add(ParameterResult("Scan4", logData.scan4))
            parameters.add(ParameterResult("ScanResult", logData.scanResult))
            parameters.add(ParameterResult("Reason", logData.reason))
            parameters.add(ParameterResult("PrintQty", logData.printingQty))*/

            val ps = ConnectionClass.setConnection(connection!!, "HT_CONFIRM_SCAN_OFFLINE", parameters)
            resultSet = ps.executeQuery()
            return if (resultSet.next()) {
                val isSuccess = resultSet.getBoolean(context.getString(R.string.txt_is_success))
                if (isSuccess) {
                    ResponseResult(true)
                } else {
                    val isMessage = resultSet.getString(context.getString(R.string.txt_is_message))
                    ResponseResult(false, isMessage)
                }
            } else {
                ResponseResult(false, context.getString(R.string.txt_false_result))
            }
        } catch (e: SQLException) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } catch (e: Exception) {
            return ResponseResult(false, context.getString(R.string.txt_exception) + e.message.toString())
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }
}