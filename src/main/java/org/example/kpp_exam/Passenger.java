package org.example.kpp_exam;


import java.util.ArrayList;
import java.util.List;

public class Passenger extends User{
    private final List<Request> requests;
    public Passenger(int id, String name){
        super(id,name);
        this.requests=new ArrayList<>();
    }
    public List<Car> findCar(String city, List<Car> carList){
        List<Car> availableCars=new ArrayList<>();
        for(Car car:carList){
            if(car.getCity().equalsIgnoreCase(city) && car.getFreeSlots()>0){
                availableCars.add(car);
            }
        }
        return availableCars;
    }
    public List<Request> getRequests(){
        return requests;
    }
}
