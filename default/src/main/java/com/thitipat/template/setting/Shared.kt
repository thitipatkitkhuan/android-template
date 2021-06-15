package com.thitipat.template.setting

import android.content.Context
import android.view.View
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.thitipat.template.R

class Shared {

    companion object {
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
    }
}