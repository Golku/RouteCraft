package com.example.routecraft.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.routecraft.data.pojos.Route;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM Route WHERE routeName == :routeName")
    Route get(String routeName);

    @Insert
    void insert(Route route);

    @Update
    void update(Route route);

    @Delete
    void delete(Route route);

    @Query("SELECT * FROM Route ORDER BY selected DESC")
    LiveData<List<Route>> getAllRoutes();
}
