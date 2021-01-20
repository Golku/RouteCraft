package com.example.routecraft.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.routecraft.data.pojos.Address;
import com.example.routecraft.data.pojos.RouteAddressCrossRef;
import com.example.routecraft.data.pojos.RouteWithAddresses;

import java.util.List;


@Dao
public interface AddressDao {

    @Query("SELECT * FROM Address WHERE lower(street) = :street AND lower(city) = :city")
    Address get(String street, String city);

    @Query("SELECT * FROM Address")
    List<Address> getAllAddress();

    @Insert
    void insert(Address address);

    @Insert
    void insertRouteAddressCrossRef(RouteAddressCrossRef routeAddressCrossRef);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);
}
