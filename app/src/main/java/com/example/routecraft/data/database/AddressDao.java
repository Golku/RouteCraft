package com.example.routecraft.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.routecraft.data.pojos.Address;


@Dao
public interface AddressDao {

    @Query("SELECT * FROM Address WHERE addressId == :id")
    Address getById(int id);

    @Query("SELECT * FROM Address WHERE address == :address")
    Address getByName(String address);

    @Insert
    void insert(Address address);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);
}
