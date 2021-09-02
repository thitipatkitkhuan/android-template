package com.thitipat.template.modules.alert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.thitipat.template.databinding.ActivityWarningBinding
import com.thitipat.template.utils.Shared

class WarningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWarningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null) {
            binding.tvMessage.text = extras.getString(Shared.KEY_MESSAGE)
        }

        binding.btnYes.setOnClickListener {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            305 -> {
                finish()
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                onBackPressed()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }
}