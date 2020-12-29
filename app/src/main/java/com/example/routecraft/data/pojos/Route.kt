package com.example.routecraft.data.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route(
        @PrimaryKey
        val id: Int,
        val name: String,
) {
    var selected: Boolean = true
    var addressIdList: String = "{\"idList\":[]}"
    var driveIdList: String = "{\"idList\":[]}"
    var creationDate: Long = System.currentTimeMillis()

//    fun copyRoute(route: Route, name: String): Route{
//
//    }
}