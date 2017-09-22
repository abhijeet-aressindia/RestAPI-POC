package com.example.android.restapipoc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "order")

public class OrderResponce {

    @DatabaseField(columnName = "orderID", id = true)
    @SerializedName("OrderID")
    @Expose
    private Integer orderID;

    @DatabaseField(columnName = "customerID")
    @SerializedName("CustomerID")
    @Expose
    private String customerID;

    @DatabaseField(columnName = "employeeID")
    @SerializedName("EmployeeID")
    @Expose
    private Integer employeeID;

    @DatabaseField(columnName = "OrderDate")
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;

    @DatabaseField(columnName = "requiredDate")
    @SerializedName("RequiredDate")
    @Expose
    private String requiredDate;

    @DatabaseField(columnName = "shippedDate")
    @SerializedName("ShippedDate")
    @Expose
    private String shippedDate;

    @DatabaseField(columnName = "shipVia")
    @SerializedName("ShipVia")
    @Expose
    private Integer shipVia;

    @DatabaseField(columnName = "freight")
    @SerializedName("Freight")
    @Expose
    private Double freight;

    @DatabaseField(columnName = "shipName")
    @SerializedName("ShipName")
    @Expose
    private String shipName;

    @DatabaseField(columnName = "shipAddress")
    @SerializedName("ShipAddress")
    @Expose
    private String shipAddress;

    @DatabaseField(columnName = "shipCity")
    @SerializedName("ShipCity")
    @Expose
    private String shipCity;

    @DatabaseField(columnName = "shipRegion")
    @SerializedName("ShipRegion")
    @Expose
    private String shipRegion;

    @DatabaseField(columnName = "shipPostalCode")
    @SerializedName("ShipPostalCode")
    @Expose
    private String shipPostalCode;

    @DatabaseField(columnName = "shipCountry")
    @SerializedName("ShipCountry")
    @Expose
    private String shipCountry;

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Integer getShipVia() {
        return shipVia;
    }

    public void setShipVia(Integer shipVia) {
        this.shipVia = shipVia;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipRegion() {
        return shipRegion;
    }

    public void setShipRegion(String shipRegion) {
        this.shipRegion = shipRegion;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

}