package org.example.kpp_exam;

public class Payment {
    public static double calculateCommission(double amount){
        return amount*0.1;
    }
    public static double processPayment(double amount, double commission){
        return amount-commission;
    }
}
