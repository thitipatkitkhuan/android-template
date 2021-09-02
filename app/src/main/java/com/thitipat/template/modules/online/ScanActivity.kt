package com.thitipat.template.modules.online

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thitipat.template.R
import com.thitipat.template.databinding.ActivityScanBinding
import com.thitipat.template.modules.ViewModelFactory
import com.thitipat.template.modules.alert.ErrorActivity
import com.thitipat.template.modules.alert.SuccessActivity
import com.thitipat.template.modules.online.viewmodel.ScanRepository
import com.thitipat.template.modules.online.viewmodel.ScanViewModel
import com.thitipat.template.utils.Shared

class ScanActivity : AppCompatActivity(), View.OnKeyListener {

    private lateinit var binding: ActivityScanBinding
    private lateinit var viewModel: ScanViewModel
    //private var kProgressHUD: KProgressHUD? = null

    //private var kanbanData: KanbanData? = null
    private var running: String = ""

    companion object {
        const val REQUEST_PICK_COLOR_3 = 100
        const val REQUEST_PICK_COLOR_4 = 101
        const val KEY_PICK_COLOR = "key_pick_color"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
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
        binding.edtScan1.setOnKeyListener(this)
        binding.edtScan2.setOnKeyListener(this)
    }

    private fun initViewModel() {
        val repository = ScanRepository(this)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ScanViewModel::class.java)

        viewModel.scan1Result.observe(this, Observer {
            showLoading(false)
            if (it.isSuccess) {
                //kanbanData = it.kanbanData

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
            if (it.isSuccess) {
                //running = it.running.toString()
                Toast.makeText(this, "$running", Toast.LENGTH_SHORT).show()
            } else {
                binding.edtScan2.requestFocus()
                binding.edtScan2.selectAll()

                getDialog(it.isMessage.toString(), false)
            }
        })

        viewModel.confirmScan.observe(this, Observer {
            showLoading(false)
            if (it.isSuccess) {
                getDialog(getString(R.string.txt_ok), true)
                clearInputDefault()
            } else {
                binding.edtScan2.requestFocus()
                binding.edtScan2.selectAll()
                getDialog(it.isMessage.toString(), false)
            }
        })
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        when (v!!.id) {
            R.id.edt_scan_1 -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_UP) {
                    val barcodeScan1 = binding.edtScan1.text.toString()
                    when {
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
                    val barcodeScan1 = binding.edtScan1.text.toString()
                    val barcodeScan2 = binding.edtScan2.text.toString()
                    when {
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
                            viewModel.scan2(barcodeScan1, barcodeScan2)
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
        binding.edtScan1.isEnabled = true
        binding.edtScan2.isEnabled = false
        binding.edtScan1.text.clear()
        binding.edtScan2.text.clear()
        binding.edtScan1.hint = getString(R.string.txt_kanban)
        binding.edtScan2.hint = getString(R.string.txt_kanban)
        binding.edtScan1.requestFocus()
    }

    private fun getDialog(message: String, bool: Boolean) {
        if (bool) {
            val intent = Intent(this, SuccessActivity::class.java)
            intent.putExtra(Shared.KEY_MESSAGE, message)
            startActivity(intent)
        } else {
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