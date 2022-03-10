package com.example.test20211227;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Delivery implements Serializable {
    String foodName;
    String date;
    String deliveryManId;
    String deliveryAddress;
    String restaurantAddress;
    String means;
    String etc;
    public Delivery() {
        this.foodName = "";
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        this.date = simpleDate.format(new Date(System.currentTimeMillis()));
        this.deliveryAddress = "";
        this.restaurantAddress = "";
        this.means = "";
        this.etc = "";
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "foodName='" + foodName + '\'' +
                ", date='" + date + '\'' +
                ", deliveryManId='" + deliveryManId + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", means='" + means + '\'' +
                ", etc='" + etc + '\'' +
                '}';
    }

    public Delivery(String foodName, String deliveryAddress, String restaurantAddress, String means, String etc) {
        this.foodName = foodName;
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        this.date = simpleDate.format(new Date(System.currentTimeMillis()));
        this.deliveryAddress = deliveryAddress;
        this.restaurantAddress = restaurantAddress;
        this.means = means;
        this.etc = etc;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(String deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }


    public void insert(Connection con,String DeliveryManId) {
        String InsertSQL = "insert into Delivery values (?,?,?,?,?,?,?)";
        PreparedStatement ist;
        try {
            ist = con.prepareStatement(InsertSQL);
            ist.setString(1, this.getFoodName());
            ist.setString(2, this.getDate());
            ist.setString(3, DeliveryManId);
            ist.setString(4, this.getDeliveryAddress());
            ist.setString(5, this.getRestaurantAddress());
            ist.setString(6, this.getMeans());
            ist.setString(7, this.getEtc());
            ist.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("배달 DB 오류");
        }
    }

    public static ArrayList<Delivery> select(Connection con, String ID) {
        String SearchSQL = "select * from Delivery where DeliveryManId = '"+ ID + "'";
        PreparedStatement sst = null;
        ArrayList<Delivery> lists = new ArrayList<>();
        Delivery member = null;
        try {
            sst = con.prepareStatement(SearchSQL);
            ResultSet rs = sst.executeQuery();
            while (rs.next()) {
                member = new Delivery();
                member.setFoodName(rs.getString("foodname"));
                member.setDate(rs.getString("Date"));
                member.setDeliveryAddress(rs.getString("deliveryaddress"));
                member.setDeliveryManId(rs.getString("deliveryManId"));
                member.setRestaurantAddress(rs.getString("RestaurantAddress"));
                member.setMeans(rs.getString("means"));
                member.setEtc(rs.getString("etc"));

                lists.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}