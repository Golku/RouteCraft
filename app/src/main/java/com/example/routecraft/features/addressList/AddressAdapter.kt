package com.example.routecraft.features.addressList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.routecraft.data.pojos.Address
import com.example.routecraft.databinding.ItemAddressBinding

class AddressAdapter(private val listener: Listener) : ListAdapter<Address,
        AddressAdapter.AddressViewHolder>(DiffCallback()){

    interface Listener {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentPrediction = getItem(position)
        holder.bind(currentPrediction)
    }

    inner class AddressViewHolder(private val binding: ItemAddressBinding, private val listener: Listener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.streetName.text = address.street
            binding.cityName.text = address.city
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Address>() {

        override fun areItemsTheSame(oldItem: Address, newItem: Address) =
                oldItem.addressId == newItem.addressId

        override fun areContentsTheSame(oldItem: Address, newItem: Address) =
                oldItem == newItem
    }
}