package com.example.routecraft.data.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
        @PrimaryKey
        val addressId: Int,
        val address: String = "",
        val street: String,
        val postCode: String = "",
        val city: String,
        val country: String = "",
        val lat: Double = 0.0,
        val lng: Double = 0.0
)