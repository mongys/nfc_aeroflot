package com.example.peresbor;


public class AircraftItem {
    private int id;
    private String aircraftType;
    private String itemName;
    private String itemLocation;
    private String startDate;
    private String endDate;

    private boolean isOnBoard;
    public AircraftItem(int id, String aircraftType, String itemName, String itemLocation, String startDate, String endDate, boolean isOnBoard) {
        this.id = id;
        this.aircraftType = aircraftType;
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOnBoard=isOnBoard;
    }
    public String getItemName() {
        return itemName;
    }


    public int getId(){return id;}
    public String getItemLocation() {
        return itemLocation;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }
    public boolean isOnBoard() {
        return isOnBoard;
    }
    public String isOnBoardStr() {
        String isOnBoardString = String.valueOf(isOnBoard);
        return isOnBoardString;
    }

    public void setOnBoard(boolean onBoard) {
        isOnBoard = onBoard;
    }
}
