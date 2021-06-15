package com.thitipat.template.setting

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.kaopiz.kprogresshud.KProgressHUD
import com.thitipat.template.R
import com.thitipat.template.databinding.ActivitySettingsBinding
import com.thitipat.template.services.ConnectionClass

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private var kProgressHUD: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnTest.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val prefTimeout: EditTextPreference? = findPreference(getString(R.string.txt_key_timeout))
            prefTimeout?.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.selectAll()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_test -> {
                testServerConfiguration()
            }
            R.id.btn_back -> {
                finish()
            }
        }
    }

    private fun testServerConfiguration() {
        val pref = Shared.getSettingPrefs(this)
        try {
            loadProgress(true)
            val connection = ConnectionClass.openConnection(pref.prefServer, pref.prefPort, pref.prefDatabase, pref.prefUser, pref.prefPassword, pref.prefTimeout)
            if (connection != null) Shared.snackBar(binding.root, getString(R.string.txt_successful))
            else Shared.snackBar(binding.root, getString(R.string.txt_failed))
            connection?.close()
        } catch (ex: Exception) {
            Shared.snackBar(binding.root, "${getString(R.string.txt_failed)}: ${ex.message.toString()}")
        } finally {
            loadProgress(false)
        }
    }

    private fun loadProgress(isShow: Boolean) {
        when (isShow) {
            true -> kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(resources.getString(R.string.txt_loading))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
            false -> kProgressHUD?.dismiss()
        }
    }

}