package com.example.golan;

import java.util.Comparator;

public class Item implements Comparable<Item> {
    private String date;
    private int bug1;
    private int bug2;
    private String notes;
    private String by;


    public Item(String date, int bug1, int bug2, String notes, String by) {
        this.date = date;
        this.bug1 = bug1;
        this.bug2 = bug2;
        this.notes = notes;
        this.by = by;
    }

    public String getDate() {
        return date;
    }

    public int getBug1() {
        return bug1;
    }

    public int getBug2() {
        return bug2;
    }

    public String getNotes() {
        return notes;
    }

    public String getBy() {
        return by;
    }


    @Override
    public int compareTo(Item item) {
        return item.getDate().compareTo(getDate());

    }
}
