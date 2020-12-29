package com.example.routecraft.features.main

import com.example.routecraft.data.pojos.Route

class CopyMachine {

    fun copyRoute(route: Route): Route{
        return route.copy()
    }
}