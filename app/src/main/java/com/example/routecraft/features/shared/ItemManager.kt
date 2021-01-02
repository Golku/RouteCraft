package com.example.routecraft.features.shared

import com.example.routecraft.data.pojos.Route

class ItemManager {

    fun createRoute(id: Int, name: String): Route {
        return Route(id, name)
    }

    fun copyRoute(route: Route, name: String): Route{
        return route.copy(name = name)
    }

    fun copyRoute(route: Route, selected: Boolean): Route{
        return route.copy(selected = selected)
    }
}