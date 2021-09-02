package com.thitipat.template.modules.alert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.thitipat.template.databinding.ActivitySuccessBinding
import com.thitipat.template.utils.Shared

class SuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Shared.soundAlert(applicationContext, Shared.SND.INFO)

        val extras = intent.extras
        if (extras != null)
            binding.tvMessage.text = extras.getString(Shared.KEY_MESSAGE)

        /*Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 1500)*/

        binding.btnOk.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {}

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