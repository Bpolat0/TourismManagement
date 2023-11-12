package com.tourismmanagement.Helper;

public class Item {
    private int key;
    private int roomNumber;
    private String value;

    public Item(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public Item() {

    }

    public Item(int id, int roomNumber) {
        this.key = id;
        this.roomNumber = roomNumber;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
