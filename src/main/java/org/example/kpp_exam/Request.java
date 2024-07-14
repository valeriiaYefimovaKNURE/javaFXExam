package org.example.kpp_exam;

public class Request {
    private final Passenger passenger;
    private final Car car;
    private String status;
    private boolean isPaid;
    public Request(Passenger passenger, Car car){
        this.passenger=passenger;
        this.car=car;
        this.status="Pending";
        this.isPaid=false;
    }
    public Passenger getPassenger() {
        return passenger;
    }

    public Car getCar() {
        return car;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public boolean getIsPaid(){
        return isPaid;
    }
    public void setIsPaid(boolean isPaid){
        this.isPaid=isPaid;
    }
    @Override
    public String toString() {
        if(getIsPaid()){
            return getPassenger().getName()+" хоче поїхати з вами до "+getCar().getCity()+", Status: "+getStatus()+". Оплачено";
        }
        else{
            return getPassenger().getName()+" хоче поїхати з вами до "+getCar().getCity()+", Status: "+getStatus();
        }
    }
}
