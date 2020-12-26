package com.example.routecraft.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.routecraft.data.pojos.Drive;


@Dao
public interface DriveDao {

    @Query("SELECT * FROM Drive WHERE id == :id")
    Drive getById(int id);

    @Query("SELECT * FROM Drive WHERE originAddress == :origin AND destinationAddress == :destination")
    Drive getByOriginAndDestination(String origin, String destination);

    @Insert
    void insert(Drive drive);

    @Update
    void update(Drive drive);

    @Delete
    void delete(Drive drive);
}
