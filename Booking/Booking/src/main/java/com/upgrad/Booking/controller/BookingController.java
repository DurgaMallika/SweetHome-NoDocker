package com.upgrad.Booking.controller;

import com.upgrad.Booking.dao.BookingDAO;
import com.upgrad.Booking.dto.BookingDTO;
import com.upgrad.Booking.dto.PaymentDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/booking")
public class BookingController {

    private BookingService bookingService;

    private ModelMapper modelMapper;

    @Autowired
    public BookingController(BookingService bookingService, ModelMapper modelMapper) {
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingInfoEntity> saveBookingInfo(@RequestBody BookingDTO bookingDTO){
        BookingInfoEntity newBookingInfo = modelMapper.map(bookingDTO, BookingInfoEntity.class);
        BookingInfoEntity savedBookingInfo = bookingService.acceptBookingDetails(newBookingInfo);

        BookingDTO savedBookingInfoDto =modelMapper.map(savedBookingInfo,BookingDTO.class);

        return new ResponseEntity(savedBookingInfoDto, HttpStatus.CREATED);
    }

    @PostMapping(value="/{bookingId}/transaction",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingInfoEntity> confirmBooking(@PathVariable(name="bookingId") int id , @RequestBody PaymentDTO paymentDTO){

        BookingInfoEntity updatedBookingInfo = bookingService.confirmBooking(paymentDTO);

        BookingDTO confirmedBookingDto = modelMapper.map(updatedBookingInfo, BookingDTO.class);

        return new ResponseEntity(confirmedBookingDto, HttpStatus.CREATED);
    }
}
