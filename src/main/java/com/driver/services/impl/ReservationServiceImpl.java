package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user;
       try{
          user= userRepository3.findById(userId).get();
       }catch (Exception e){
           throw new Exception("sorry user does not exist");
       }
        ParkingLot parkingLot;
       try{
           parkingLot= parkingLotRepository3.findById(parkingLotId).get();
       } catch(Exception e){
           throw new Exception("Sorry parkingLot does not exist!");
       }

       List<Spot>spotList=parkingLot.getSpots();
       boolean checkForSpots=false;
       for(Spot spot:spotList){
          if(spot.isOccupied()==false){
              checkForSpots=true;
              break;
          }
       }
       if(!checkForSpots){
           throw new Exception("Cannot make reservation");
       }
       SpotType requestSpotType;
       if(numberOfWheels>4){
           requestSpotType=SpotType.OTHERS;
       } else if(numberOfWheels>2){
           requestSpotType=SpotType.FOUR_WHEELER;
       } else{
           requestSpotType=SpotType.TWO_WHEELER;
       }
       int minimumPrice=Integer.MAX_VALUE;
       checkForSpots=false;
       Spot spotChosen=null;
       for(Spot spot:spotList){
           if(requestSpotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)){
               if(spot.getPricePerHour()*timeInHours<minimumPrice && !spot.isOccupied()){
                   minimumPrice=spot.getPricePerHour()*timeInHours;
                   checkForSpots=true;
                   spotChosen=spot;
               }
           }else if(requestSpotType.equals(SpotType.FOUR_WHEELER)&& spot.getSpotType().equals(SpotType.OTHERS)|| spot.getSpotType().equals(SpotType.FOUR_WHEELER)){
               if(spot.getPricePerHour()*timeInHours<minimumPrice && !spot.isOccupied()){
                   minimumPrice=spot.getPricePerHour()*timeInHours;
                   checkForSpots=true;
                   spotChosen=spot;
               }
           }
           else if(requestSpotType.equals(SpotType.TWO_WHEELER)&& spot.getSpotType().equals(SpotType.OTHERS)||spot.getSpotType().equals(SpotType.FOUR_WHEELER)||spot.getSpotType().equals(SpotType.TWO_WHEELER)){
               if(spot.getPricePerHour()*timeInHours<minimumPrice && !spot.isOccupied()){
                   minimumPrice=spot.getPricePerHour()*timeInHours;
                   checkForSpots=true;
                   spotChosen=spot;
               }
           }
       }
        if(!checkForSpots){
            throw new Exception("Cannot make reservation");
        }
        assert spotChosen!=null;
        spotChosen.setOccupied(true);

        Reservation reservation=new Reservation();
        reservation.setNumberOfHour(timeInHours);
        reservation.setSpot(spotChosen);
        reservation.setUser(user);

        spotChosen.getReservations().add(reservation);
        user.getReservationList().add(reservation);
        userRepository3.save(user);
        spotRepository3.save(spotChosen);
        return reservation;
    }
}
