package org.example.kpp_exam;

import java.util.ArrayList;
import java.util.List;

public class BlaBlaCar {
    private final List<Passenger> passengers;
    private final List<Car> carList;
    private final List<Driver> drivers;
    public BlaBlaCar(){
        this.carList=new ArrayList<>();
        this.drivers=new ArrayList<>();
        this.passengers=new ArrayList<>();
    }
    public void registerPassenger(Passenger passenger){
        passengers.add(passenger);
    }
    public void registerDriver(Driver driver){
        drivers.add(driver);
    }
    public void addCar(Car car){
        carList.add(car);
    }
    public void blockUser(User user){
        user.BlockUser();
    }
    public void unblockUser(User user){
        user.UnblockUser();
    }
    public List<Car> getCarList(){
        return carList;
    }
    public List<Passenger> getPassengerList(){
        return passengers;
    }
    public List<Driver> getDriverList(){
        return drivers;
    }
    public void makeRequest(Passenger passenger,Car car){
        Request request=new Request(passenger,car);
        for(Driver driver:drivers){
            if(driver.getName().equals(car.getDriver())){
                passenger.getRequests().add(request);
                driver.addRequest(request);
                break;
            }
        }
    }
    public void processPayment(Car car){
        double commission=Payment.calculateCommission(car.getPrice());
        double payment=Payment.processPayment(car.getPrice(),commission);
        System.out.println("Car: "+car.getId()+", Driver receives: " + payment);
        System.out.println("Commission: " + commission);
    }
}
