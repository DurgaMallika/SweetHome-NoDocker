package com.upgrad.Booking.services;

import com.upgrad.Booking.dto.PaymentDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookingService {

    public BookingInfoEntity acceptBookingDetails(BookingInfoEntity bookingInfoEntity);

    public BookingInfoEntity updateTransactionId(int id, int transactionId);

    public BookingInfoEntity getBookingDetails(int id);

    public ArrayList<String> getRoomNumbers(int numOfRooms);

    public int getRoomPrice(int numOfRooms, LocalDate fromDate, LocalDate toDate);

    public BookingInfoEntity confirmBooking(PaymentDTO paymentDTO);

    public void sendNotification(BookingInfoEntity updatedBookingInfoEntity);

}
