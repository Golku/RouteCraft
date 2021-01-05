package com.example.routecraft.features.addAddressWithAutocomplete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.routecraft.data.pojos.AutocompletePrediction
import com.example.routecraft.databinding.ItemAutocompletePredictionBinding

class AutocompleteAdapter(private val listener: Listener) : ListAdapter<AutocompletePrediction, AutocompleteAdapter.PredictionViewHolder>(DiffCallback()) {

    interface Listener {
        fun predictionClicked()
        fun predictionSelected()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val binding = ItemAutocompletePredictionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PredictionViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val currentPrediction = getItem(position)
        holder.bind(currentPrediction)
    }

    inner class PredictionViewHolder(private val binding: ItemAutocompletePredictionBinding, private val listener: Listener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(prediction: AutocompletePrediction) {
            binding.streetNameTv.text = prediction.streetName
            binding.cityNameTv.text = prediction.cityName
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AutocompletePrediction>() {

        override fun areItemsTheSame(oldItem: AutocompletePrediction, newItem: AutocompletePrediction) =
                oldItem.streetName == newItem.streetName

        override fun areContentsTheSame(oldItem: AutocompletePrediction, newItem: AutocompletePrediction) =
                oldItem == newItem
    }
}