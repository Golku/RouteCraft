package com.example.routecraft.data.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route(
        @PrimaryKey
        val routeId: Int,
        val name: String,
        val selected: Boolean = true,
        val addressIdList: String = "{\"idList\":[]}",
        val driveIdList: String = "{\"idList\":[]}",
        val creationDate: Long = System.currentTimeMillis()
)