package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;
    @Autowired
    ReservationServiceImpl reservationService;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();

        Spot spot=reservation.getSpot();
        int bill=spot.getPricePerHour()*reservation.getNumberOfHour();
        if(amountSent<bill){
            throw new Exception("Insufficient balance");
        }
        if(mode.equalsIgnoreCase("cash")||mode.equalsIgnoreCase("card")||mode.equalsIgnoreCase("upi")){
            Payment payment=new Payment();
            if(mode.equalsIgnoreCase("cash")){
                payment.setPaymentMode(PaymentMode.CASH);
            }
            else if(mode.equalsIgnoreCase("card")){
                payment.setPaymentMode(PaymentMode.CARD);
            }
            else{
                payment.setPaymentMode(PaymentMode.UPI);
            }
            payment.setPaymentCompleted(true);
            payment.setReservation(reservation);
            reservation.setPayment(payment);
            reservationRepository2.save(reservation);
            return payment;
        }
        else{
            throw new Exception("payment mode not detected");
        }


    }
}