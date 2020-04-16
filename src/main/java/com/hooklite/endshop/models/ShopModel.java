package com.hooklite.endshop.models;

import java.util.ArrayList;

public class ShopModel {
    private String name;
    private String currency;
    private ArrayList<ItemModel> items = new ArrayList<>();
    private int slot;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public ArrayList<ItemModel> getItems() { return items; }
    public void setItems(ArrayList<ItemModel> items) { this.items = items; }
    public int getSlot() { return slot; }
    public void setSlot(int slot) { this.slot = slot; }
}