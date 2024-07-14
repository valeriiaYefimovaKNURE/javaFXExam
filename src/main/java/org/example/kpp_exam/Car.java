package org.example.kpp_exam;

public class Car {
    private final int id;
    private int freeSlots;
    private final String driver;
    private final String city;
    private final double price;
    public Car(int id, int freeSlots, String driver, String city,double price){
        this.id=id;
        this.freeSlots=freeSlots;
        this.driver=driver;
        this.city=city;
        this.price=price;
    }
    public int getId(){
        return id;
    }
    public int getFreeSlots(){
        return freeSlots;
    }
    public String getCity(){
        return city;
    }
    public void setFreeSlots(int free){
        this.freeSlots=free;
    }
    public String getDriver(){
        return driver;
    }
    public double getPrice(){
        return price;
    }
    @Override
    public String toString() {
        return "Водій: " + driver + ", Місце призначення: " + city + ", Місця: " + freeSlots + ", Ціна: " + price;
    }
}
