package com.example.routecraft.data.pojos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RouteWithAddresses(
        @Embedded val route: Route,
        @Relation(
                parentColumn = "routeId",
                entityColumn = "addressId",
                associateBy = Junction(RouteAddressCrossRef::class)
        )
        val address: List<Address>
)