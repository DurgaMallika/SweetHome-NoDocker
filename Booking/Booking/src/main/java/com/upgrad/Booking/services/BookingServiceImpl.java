package com.upgrad.Booking.services;

import com.upgrad.Booking.dao.BookingDAO;
import com.upgrad.Booking.dto.PaymentDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.InvalidInputException;
import com.upgrad.Booking.exception.RecordNotFoundException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService{

    //paymentserviceurl value is set in application.properties
    @Value("${paymentService.url}")
    private String paymentServiceUrl;

    //topicname value is set in application.properties
    @Value("${topicName}")
    private String topic;

    private BookingDAO bookingDAO;

    private RestTemplate restTemplate;

    private Producer<String , String> producer;

    @Autowired
    public BookingServiceImpl(BookingDAO bookingDAO, RestTemplate restTemplate, Producer<String, String> producer) {
        this.bookingDAO = bookingDAO;
        this.restTemplate = restTemplate;
        this.producer = producer;
    }

    @Override
    public BookingInfoEntity acceptBookingDetails(BookingInfoEntity bookingInfoEntity) {
        bookingInfoEntity.setRoomNumbers(getRoomNumbers(bookingInfoEntity.getNumOfRooms()).toString());
        bookingInfoEntity.setRoomPrice(getRoomPrice(bookingInfoEntity.getNumOfRooms(),bookingInfoEntity.getFromDate(),bookingInfoEntity.getToDate()));
        bookingInfoEntity.setBookedOn(LocalDate.now());
        return bookingDAO.save(bookingInfoEntity);
    }

    @Override
    public BookingInfoEntity updateTransactionId(int id, int transactionId) {
        BookingInfoEntity savedBookingInfo = getBookingDetails(id);
        savedBookingInfo.setTransactionId(transactionId);
        bookingDAO.save(savedBookingInfo);

        return savedBookingInfo;
    }

    @Override
    public BookingInfoEntity getBookingDetails(int id) {
        return bookingDAO.findById(id).get();
    }

    @Override
    public ArrayList<String> getRoomNumbers(int numOfRooms) {
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<numOfRooms; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }

    @Override
    public int getRoomPrice(int numOfRooms, LocalDate fromDate, LocalDate toDate) {
        long noOfDays = ChronoUnit.DAYS.between(fromDate,toDate);
        return (int) (1000 * numOfRooms * noOfDays);
    }

    @Override
    public BookingInfoEntity confirmBooking(PaymentDTO paymentDTO) {

        int transactionId = 0;

        //Check whether Payment mode is selected as UPI/CARD else throw exception
        if((paymentDTO.getPaymentMode().equalsIgnoreCase("UPI" )) || (paymentDTO.getPaymentMode().equalsIgnoreCase("CARD"))){

            //Check if Booking Id is present in Database or not and throw exception if BookingId is invalid
            Optional<BookingInfoEntity> bookingId = bookingDAO.findById(paymentDTO.getBookingId());
            if(!bookingId.isPresent())
                throw new RecordNotFoundException("Invalid Booking Id");

            //Gets transaction Id from payment service and saves it in the BookingInfoEntity table
            transactionId = restTemplate.postForObject(paymentServiceUrl, paymentDTO , Integer.class);
        }
        else{
                throw new InvalidInputException("Invalid mode of payment");
        }

        //Invoking updateTransactionId method to save transaction id in DB
        BookingInfoEntity updatedBookingInfo = updateTransactionId(paymentDTO.getBookingId(), transactionId);
        //Calling sendNotification method
        sendNotification(updatedBookingInfo);

        return updatedBookingInfo;

    }

    //After successful payment, sends notification to the kafka "message" topic
    @Override
    public void sendNotification(BookingInfoEntity bookingInfo){

        String key = "Booking" + bookingInfo.getBookingId();
        String value = "Booking confirmed for user with aadhaar number: " + bookingInfo.getAadharNumber()
                +    "    |    "  +
                "Here are the booking details:    " + bookingInfo.toString();

        producer.send(new ProducerRecord<String , String>(topic, key, value));

    }
}
