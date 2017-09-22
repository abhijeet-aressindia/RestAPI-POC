package com.example.android.restapipoc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "customer")
public class CustomerResponse {
    @DatabaseField(columnName = "customerID",id = true)
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @DatabaseField(columnName = "companyName")
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @DatabaseField(columnName = "contactName")
    @SerializedName("ContactName")
    @Expose
    private String contactName;
    @DatabaseField(columnName = "contactTitle")
    @SerializedName("ContactTitle")
    @Expose
    private String contactTitle;
    @DatabaseField(columnName = "address")
    @SerializedName("Address")
    @Expose
    private String address;
    @DatabaseField(columnName = "city")
    @SerializedName("City")
    @Expose
    private String city;
    @DatabaseField(columnName = "region")
    @SerializedName("Region")
    @Expose
    private String region;
    @DatabaseField(columnName = "postalCode")
    @SerializedName("PostalCode")
    @Expose
    private String postalCode;
    @DatabaseField(columnName = "country")
    @SerializedName("Country")
    @Expose
    private String country;
    @DatabaseField(columnName = "phone")
    @SerializedName("Phone")
    @Expose
    private String phone;
    @DatabaseField(columnName = "fax")
    @SerializedName("Fax")
    @Expose
    private String fax;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }



    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

}