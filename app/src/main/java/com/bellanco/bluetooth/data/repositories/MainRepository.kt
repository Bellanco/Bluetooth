package com.bellanco.bluetooth.data.repositories

import android.bluetooth.BluetoothDevice
import com.bellanco.bluetooth.services.BluetoothScanner
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository  @Inject constructor(
    private val bluetoothScanner: BluetoothScanner
) {

    // Métodos para Bluetooth
    fun getBluetoothDevices(): Flow<BluetoothDevice?> {
        return bluetoothScanner.devices
    }

    fun startBluetoothScan() {
        bluetoothScanner.startScan()
    }

    fun stopBluetoothScan() {
        bluetoothScanner.stopScan()
    }
}