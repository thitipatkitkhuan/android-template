package com.thitipat.template.modules.offline

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thitipat.template.R
import com.thitipat.template.databinding.ActivityScanEmergencyBinding
import com.thitipat.template.modules.ViewModelFactory
import com.thitipat.template.modules.alert.ErrorActivity
import com.thitipat.template.modules.alert.SuccessActivity
import com.thitipat.template.modules.offline.viewmodel.ScanEmergencyRepository
import com.thitipat.template.modules.offline.viewmodel.ScanEmergencyViewModel
import com.thitipat.template.room.ItemData
import com.thitipat.template.room.MainDatabase
import com.thitipat.template.utils.Shared

class ScanEmergencyActivity : AppCompatActivity(), View.OnKeyListener {

    private lateinit var binding: ActivityScanEmergencyBinding
    private lateinit var viewModel: ScanEmergencyViewModel
    //private var kProgressHUD: KProgressHUD? = null

    private var kanbanData: ItemData? = null

    companion object {
        const val REQUEST_PICK_COLOR_3 = 100
        const val REQUEST_PICK_COLOR_4 = 101
        const val KEY_PICK_COLOR = "key_pick_color"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initEvent()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> {
                clearInputDefault()
            }
        }
        return true
    }

    private fun initEvent() {
        clearInputDefault()
        binding.edtScan0.setOnKeyListener(this)
        binding.edtScan1.setOnKeyListener(this)
        binding.edtScan2.setOnKeyListener(this)
    }

    private fun initViewModel() {
        val dao = MainDatabase.getInstance(this).mainDao()
        val repository = ScanEmergencyRepository(dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ScanEmergencyViewModel::class.java)

        viewModel.scan0Result.observe(this, Observer {
            showLoading(false)
            if (it.isSuccess && it.isPartExists!! > 0) {
                binding.edtScan0.isEnabled = false
                binding.edtScan1.isEnabled = true
                binding.edtScan1.text.clear()
                binding.edtScan1.requestFocus()
            } else {
                binding.edtScan0.requestFocus()
                binding.edtScan0.selectAll()

                getDialog("Not found part number", false)
            }
        })

        viewModel.scan1Result.observe(this, Observer {
            showLoading(false)
            if (it.isSuccess) {

                kanbanData = it.itemData
                //Toast.makeText(this, "1.${kanbanData?.scan1}\n2.${kanbanData?.scan2}\n3.${kanbanData?.scan3}\n4.${kanbanData?.scan4}", Toast.LENGTH_SHORT).show()

                binding.edtScan1.isEnabled = false
                binding.edtScan2.isEnabled = true
                binding.edtScan2.text.clear()
                binding.edtScan2.requestFocus()
            } else {
                binding.edtScan1.requestFocus()
                binding.edtScan1.selectAll()

                getDialog(it.isMessage.toString(), false)
            }
        })

        viewModel.scan2Result.observe(this, Observer {
            showLoading(false)
            if (it.isSuccess && it.isPartExists!! > 0) {

            } else {
                binding.edtScan2.requestFocus()
                binding.edtScan2.selectAll()

                getDialog("Barcode tag is not found!", false)
            }
        })

        viewModel.confirmScan.observe(this, Observer {
            showLoading(false)
            if (it.isSuccess) {
                getDialog(getString(R.string.txt_ok), true)
                clearInputDefault()
            } else {
                /*if (kanbanData?.scan4.toString().isNotEmpty()) {
                    binding.edtScan4.requestFocus()
                    binding.edtScan4.selectAll()
                } else {
                    binding.edtScan3.requestFocus()
                    binding.edtScan3.selectAll()
                }*/
                getDialog(it.isMessage.toString(), false)
            }
        })
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        when (v!!.id) {
            R.id.edt_scan_0 -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_UP) {
                    val barcodeScan0 = binding.edtScan0.text.toString()
                    when {
                        barcodeScan0.isEmpty() -> {
                            binding.edtScan0.requestFocus()
                            binding.edtScan0.selectAll()
                            binding.edtScan0.error = getString(R.string.txt_please_scan_barcode)
                        }
                        else -> {
                            viewModel.scan0(barcodeScan0)
                            showLoading(true)
                        }
                    }
                    return true
                }
            }
            R.id.edt_scan_1 -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_UP) {
                    val barcodeScan0 = binding.edtScan0.text.toString()
                    val barcodeScan1 = binding.edtScan1.text.toString()
                    when {
                        barcodeScan0.isEmpty() -> {
                            binding.edtScan0.requestFocus()
                            binding.edtScan0.selectAll()
                            binding.edtScan0.error = getString(R.string.txt_please_scan_barcode)
                        }
                        barcodeScan1.isEmpty() -> {
                            binding.edtScan1.requestFocus()
                            binding.edtScan1.selectAll()
                            binding.edtScan1.error = getString(R.string.txt_please_scan_barcode)
                        }
                        else -> {
                            viewModel.scan1(barcodeScan1)
                            showLoading(true)
                        }
                    }
                    return true
                }
            }
            R.id.edt_scan_2 -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_UP) {
                    val barcodeScan0 = binding.edtScan0.text.toString()
                    val barcodeScan1 = binding.edtScan1.text.toString()
                    val barcodeScan2 = binding.edtScan2.text.toString()
                    when {
                        barcodeScan0.isEmpty() -> {
                            binding.edtScan0.requestFocus()
                            binding.edtScan0.selectAll()
                            binding.edtScan0.error = getString(R.string.txt_please_scan_barcode)
                        }
                        barcodeScan1.isEmpty() -> {
                            binding.edtScan1.requestFocus()
                            binding.edtScan1.selectAll()
                            binding.edtScan1.error = getString(R.string.txt_please_scan_barcode)
                        }
                        barcodeScan2.isEmpty() -> {
                            binding.edtScan2.requestFocus()
                            binding.edtScan2.selectAll()
                            binding.edtScan2.error = getString(R.string.txt_please_scan_barcode)
                        }
                        else -> {
                            viewModel.scan2(barcodeScan2)
                            showLoading(true)
                        }
                    }
                    return true
                }
            }
        }
        return false
    }

    private fun clearInputDefault() {
        binding.edtScan0.isEnabled = true
        binding.edtScan1.isEnabled = false
        binding.edtScan2.isEnabled = false
        binding.edtScan0.text.clear()
        binding.edtScan1.text.clear()
        binding.edtScan2.text.clear()
        binding.edtScan0.requestFocus()
    }

    private fun getDialog(message: String, bool: Boolean) {
        binding.lnResult.visibility = View.GONE
        if (bool) {
            /*binding.lnResult.setBackgroundResource(R.color.green)
            binding.lnResult.visibility = View.VISIBLE
            binding.tvResult.text = message
            Handler(Looper.getMainLooper()).postDelayed({
                binding.lnResult.visibility = View.GONE
            }, 1500)*/

            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra(Shared.KEY_MESSAGE, message)
            startActivity(intent)
        } else {
            /*binding.lnResult.setBackgroundResource(R.color.red)
            binding.lnResult.visibility = View.VISIBLE*/

            val intent = Intent(this, ErrorActivity::class.java)
            intent.putExtra(Shared.KEY_MESSAGE, message)
            startActivity(intent)
        }
    }

    private fun showLoading(bool: Boolean) {
        /*if (bool) {
            kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.txt_loading))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        } else {
            if (kProgressHUD?.isShowing == true) {
                kProgressHUD?.dismiss()
            }
        }*/
    }

}