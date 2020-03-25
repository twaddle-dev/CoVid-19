package com.twaddle.covid19.model;

public class Elder {
    private String name;
    private String address;
    private String item;

    public Elder(String name, String address, String item) {
        this.name = name;
        this.address = address;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
