package com.thitipat.template.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.thitipat.template.R
import com.thitipat.template.services.ConnectionClass
import com.thitipat.template.setting.ResponseSettingsPref
import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.*

class Shared {

    companion object {
        const val KEY_MESSAGE = "message"
        const val KEY_IS_EMERGENCY = "key_emergency"

        fun snackBar(root: View, msg: String) {
            Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
        }

        fun getSettingPrefs(context: Context): ResponseSettingsPref {
            val settingPrefs = PreferenceManager.getDefaultSharedPreferences(context)

            val server = settingPrefs.getString(context.getString(R.string.txt_key_server), "")
            val port = settingPrefs.getString(context.getString(R.string.txt_key_port), "")
            val database = settingPrefs.getString(context.getString(R.string.txt_key_database), "")
            val user = settingPrefs.getString(context.getString(R.string.txt_key_user), "")
            val password = settingPrefs.getString(context.getString(R.string.txt_key_password), "")
            val timeout = settingPrefs.getString(context.getString(R.string.txt_key_timeout), "")

            return ResponseSettingsPref(
                server.toString(),
                port.toString(),
                database.toString(),
                user.toString(),
                password.toString(),
                timeout.toString()
            )
        }

        fun getDateNow(): String {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale("th", "TH"))
            val todayDate = Date()
            return currentDate.format(todayDate)
        }

        fun getTimeNow(): String {
            val currentDate = SimpleDateFormat("HH:mm:ss", Locale("th", "TH"))
            val todayDate = Date()
            return currentDate.format(todayDate)
        }

        fun replaceStr(str: String): String {
            return str.replace("@", "").replace("_", "")
        }

        fun setStart(start: Int): Int {
            return (start - 1)
        }

        fun setDigit(start: Int, digit: Int): Int {
            return (start - 1) + (digit + 1)
        }

        fun checkConnectionStatus(context: Context): Boolean {
            val pref = getSettingPrefs(context)
            var connection: Connection? = null
            return try {
                connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)
                connection != null
            } catch (ex: Exception) {
                false
            } finally {
                connection?.close()
            }
        }

        fun soundAlert(context: Context, snd: SND, isVibrate: Boolean = true) {
            val mp = when(snd){
                SND.CONFIRM ->  MediaPlayer.create(context, R.raw.snd_confirm)
                SND.DEFAULT -> MediaPlayer.create(context, R.raw.snd_default)
                SND.ERROR ->  MediaPlayer.create(context, R.raw.snd_error)
                SND.INFO -> MediaPlayer.create(context, R.raw.snd_info)
            }
            mp.start()
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(isVibrate) v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                if(isVibrate) v.vibrate(500)
            }
        }
    }

    enum class SND {
        CONFIRM,
        DEFAULT,
        ERROR,
        INFO
    }

    enum class BARCODE {
        EMERGENCY,
        KANBAN,
        TAG
    }
}