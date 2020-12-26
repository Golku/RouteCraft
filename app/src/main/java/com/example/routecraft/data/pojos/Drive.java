package com.example.routecraft.data.pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Drive {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String originAddress;
    private String destinationAddress;

    public Drive(String originAddress, String destinationAddress) {
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}
