package com.example.routecraft.data.pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Route {

    @PrimaryKey()
    private int id;
    private String routeName;
    private boolean selected;
    private String AddressIdList;
    private String driveIdList;
    private Long creationDate;

    public Route(int id, String routeName) {
        this.id = id;
        this.routeName = routeName;
        selected = true;
        AddressIdList = "{\"idList\":[]}";
        driveIdList = "{\"idList\":[]}";
        creationDate = System.currentTimeMillis();
    }

    public int getId() {
        return id;
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

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreationDate() {
        return creationDate;
    }
}
