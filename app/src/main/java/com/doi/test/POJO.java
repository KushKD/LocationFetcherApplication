package com.doi.test;

import java.io.Serializable;

public class POJO implements Serializable {

    private String Shopname;
    private String Ownername;
    private String Address;
    private String mobile;
    private String items;
    private String District;
    private String Tehsil;
    private String Town;
    private String pincode;
    private String latitude;
    private String longitude;
    private String address_updated;

    public String getShopname() {
        return Shopname;
    }

    public void setShopname(String shopname) {
        Shopname = shopname;
    }

    public String getOwnername() {
        return Ownername;
    }

    public void setOwnername(String ownername) {
        Ownername = ownername;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getTehsil() {
        return Tehsil;
    }

    public void setTehsil(String tehsil) {
        Tehsil = tehsil;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress_updated() {
        return address_updated;
    }

    public void setAddress_updated(String address_updated) {
        this.address_updated = address_updated;
    }

    @Override
    public String toString() {
        return "POJO{" +
                "Shopname='" + Shopname + '\'' +
                ", Ownername='" + Ownername + '\'' +
                ", Address='" + Address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", items='" + items + '\'' +
                ", District='" + District + '\'' +
                ", Tehsil='" + Tehsil + '\'' +
                ", Town='" + Town + '\'' +
                ", pincode='" + pincode + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address_updated='" + address_updated + '\'' +
                '}';
    }
}
