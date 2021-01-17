package com.example.routecraft.data.pojos

import androidx.room.Entity

@Entity(primaryKeys = ["routeId", "addressId"])
data class RouteAddressCrossRef(
           val routeId: Int,
           val addressId: Int
)