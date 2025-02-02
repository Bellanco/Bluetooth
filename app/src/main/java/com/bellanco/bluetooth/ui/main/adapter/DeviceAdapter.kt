package com.bellanco.bluetooth.ui.main.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bellanco.bluetooth.databinding.ItemDeviceListBinding
import timber.log.Timber

@SuppressLint("MissingPermission", "ViewHolder")
class DeviceAdapter(
    private val context: Context,
    testModelList: MutableList<BluetoothDevice> = mutableListOf()
) : ArrayAdapter<BluetoothDevice>(context, 0, testModelList) {

    private val modelList: MutableList<BluetoothDevice> = testModelList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        Timber.i("getView $position")

        val binding: ItemDeviceListBinding
        val view: View

        if (convertView == null) {
            binding = ItemDeviceListBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemDeviceListBinding
        }

        getItem(position)?.let { device ->
            binding.btListName.text = device.name ?: ""
            binding.btListAddress.text = device.address ?: ""
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
        getView(position, convertView, parent)

    fun add(model: BluetoothDevice) {
        modelList.add(model)
        notifyDataSetChanged()
    }

    override fun clear() {
        modelList.clear()
        notifyDataSetChanged()
    }
}
