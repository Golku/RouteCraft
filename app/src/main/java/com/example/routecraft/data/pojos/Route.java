package com.example.routecraft.data.pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Route {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String routeName;
    private boolean selected;
    private String AddressIdList;
    private String driveIdList;

    public Route(String routeName) {
        this.routeName = routeName;
        AddressIdList = "{\"idList\":[]}";
        driveIdList = "{\"idList\":[]}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAddressIdList() {
        return AddressIdList;
    }

    public void setAddressIdList(String addressIdList) {
        AddressIdList = addressIdList;
    }

    public String getDriveIdList() {
        return driveIdList;
    }

    public void setDriveIdList(String driveIdList) {
        this.driveIdList = driveIdList;
    }
}
