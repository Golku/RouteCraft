package com.example.routecraft.features.addAddressWithAutocomplete

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.routecraft.R
import com.example.routecraft.data.pojos.AutocompletePrediction
import com.example.routecraft.databinding.ItemAutocompletePredictionBinding

class AutocompleteAdapter(private val listener: Listener) : ListAdapter<AutocompletePrediction, AutocompleteAdapter.PredictionViewHolder>(DiffCallback()) {

    interface Listener {
        fun predictionSelected(prediction: AutocompletePrediction)
        fun predictionClick(prediction: AutocompletePrediction)
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

        init {
            binding.apply {
                infoHolder.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.predictionClick(getItem(position))
                    }
                }
                predictionBtn.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.predictionSelected(getItem(position))
                    }
                }
            }
        }

        fun bind(prediction: AutocompletePrediction) {
            binding.streetName.text = prediction.streetName
            binding.cityName.text = prediction.cityName

            if(adapterPosition==0){
                binding.infoHolder.setBackgroundColor(Color.WHITE)
            }else{
                binding.infoHolder.setBackgroundResource(R.drawable.prediction_bg)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AutocompletePrediction>() {

        override fun areItemsTheSame(oldItem: AutocompletePrediction, newItem: AutocompletePrediction) =
                false

        override fun areContentsTheSame(oldItem: AutocompletePrediction, newItem: AutocompletePrediction) =
                false
    }
}