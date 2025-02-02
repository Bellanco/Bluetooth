package com.bellanco.bluetooth.ui.main

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bellanco.bluetooth.databinding.ActivityMainBinding
import com.bellanco.bluetooth.ui.main.adapter.DeviceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupObservables(binding)

        setupView(binding)
    }

    private fun setupView(binding: ActivityMainBinding) {

        val adapter = DeviceAdapter(this)

        binding.spDevices.adapter = adapter

        binding.btStop.setOnClickListener {
            viewModel.stopBluetoothScan()
            clearAdapter(binding)
            binding.btStop.visibility = View.GONE
            binding.btStart.visibility = View.VISIBLE
        }

        requestBluetoothPermission(binding)
    }

    private fun setupObservables(binding: ActivityMainBinding) {

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.bluetoothDeviceFlow.collect {
                        it?.let { model ->
                            updateAdapter(binding, model)
                        }
                    }
                }
            }
        }
    }

    private fun requestBluetoothPermission(binding: ActivityMainBinding) {
        val requiredPermissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val requestBluetoothPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allGranted = permissions.all { it.value }
                if (allGranted) {
                    binding.btStart.setOnClickListener {
                        viewModel.startBluetoothScan()
                        binding.btStart.visibility = View.GONE
                        binding.btStop.visibility = View.VISIBLE
                    }
                } else {
                    binding.btStart.isEnabled = false
                }
            }

        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            requestBluetoothPermissionsLauncher.launch(missingPermissions.toTypedArray())
        } else {
            binding.btStart.setOnClickListener {
                viewModel.startBluetoothScan()
                binding.btStart.visibility = View.GONE
                binding.btStop.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopBluetoothScan()
    }


    private fun updateAdapter(binding: ActivityMainBinding, model: BluetoothDevice) {
        (binding.spDevices.adapter as? DeviceAdapter)?.add(model)
    }

    private fun clearAdapter(binding: ActivityMainBinding) {
        (binding.spDevices.adapter as? DeviceAdapter)?.clear()
    }
}
