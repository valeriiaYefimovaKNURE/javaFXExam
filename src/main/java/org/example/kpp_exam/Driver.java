package org.example.kpp_exam;

import java.util.ArrayList;
import java.util.List;

public class Driver extends User{
    private final List<Car> cars;
    private final List<Request> requests;
    public Driver(int id, String name){
        super(id,name);
        this.cars=new ArrayList<>();
        this.requests=new ArrayList<>();
    }
    public void addCar(Car car){
        cars.add(car);
    }
    public List<Car> getCars(){
        return cars;
    }
    public void approveRequest(Request request){
        request.setStatus("Approved");
        request.getCar().setFreeSlots(request.getCar().getFreeSlots()-1);
    }
    public void rejectRequest(Request request){
        request.setStatus("Rejected");
    }
    public void addRequest(Request request){
        requests.add(request);
    }
    public List<Request> getRequests(){
        return requests;
    }
}
