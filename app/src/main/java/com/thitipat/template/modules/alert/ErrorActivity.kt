package com.thitipat.template.modules.alert

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.thitipat.template.databinding.ActivityErrorBinding
import com.thitipat.template.utils.Shared

class ErrorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Shared.soundAlert(applicationContext, Shared.SND.ERROR)

        val extras = intent.extras
        if (extras != null) {
            binding.tvMessage.text = extras.getString(Shared.KEY_MESSAGE)
        }

        binding.btnBack.setOnClickListener {
            /*val password = binding.edtPassword.text.toString().trim()
            if (password.contentEquals(Shared.ERROR_PASSWORD)) {*/
                finish()
            /*} else {
                Toast.makeText(this, "Password is not correct", Toast.LENGTH_SHORT).show();
            }*/
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