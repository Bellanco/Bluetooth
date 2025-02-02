package com.bellanco.bluetooth.ui.main

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bellanco.bluetooth.data.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {


    val bluetoothDeviceFlow: StateFlow<BluetoothDevice?> = repository.getBluetoothDevices().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    fun startBluetoothScan() {
        repository.startBluetoothScan()
    }

    fun stopBluetoothScan() {
        repository.stopBluetoothScan()
    }

}