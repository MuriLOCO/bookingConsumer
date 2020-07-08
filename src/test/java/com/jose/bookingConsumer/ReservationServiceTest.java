package com.jose.bookingConsumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jose.bookingConsumer.dto.ReservationDTO;
import com.jose.bookingConsumer.repository.ReservationRepository;
import com.jose.bookingConsumer.service.MailSenderService;
import com.jose.bookingConsumer.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReservationServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MailSenderService mailSenderService;

    //This test shows that even if it receives concurrent requests it will process accordingly with the order it was received following the queue system.
    @Test
    public void receive_concurrent_reservation_success() throws JsonProcessingException {

        String message1 = "{\"reservationId\":\"b70c12fb-8c0a-4b06-832c-626beac35366\",\"dayOfReservation\":1594212466.336039100,\"dayOfArrival\":1595940466.336039100,\"dayOfLeaving\":1596199666.336039100,\"cancelled\":false,\"firstMame\":\"Jose\",\"lastName\":\"Mendes\",\"phoneNumber\":\"+15147999483\",\"emailAddress\":\"jose.murilo.mendes@gmail.com\",\"dateOfBirth\":531792000.000000000}";
        String message2 = "{\"reservationId\":\"b70c12fb-8c0a-4b06-832c-626beac35366\",\"dayOfReservation\":1594212466.336039100,\"dayOfArrival\":1595940466.336039100,\"dayOfLeaving\":1596199666.336039100,\"cancelled\":false,\"firstMame\":\"Jose\",\"lastName\":\"Mendes\",\"phoneNumber\":\"+15147999483\",\"emailAddress\":\"jose.murilo.mendes@gmail.com\",\"dateOfBirth\":531792000.000000000}";
        String message3 = "{\"reservationId\":\"b70c12fb-8c0a-4b06-832c-626beac35366\",\"dayOfReservation\":1594212466.336039100,\"dayOfArrival\":1595940466.336039100,\"dayOfLeaving\":1596199666.336039100,\"cancelled\":false,\"firstMame\":\"Jose\",\"lastName\":\"Mendes\",\"phoneNumber\":\"+15147999483\",\"emailAddress\":\"jose.murilo.mendes@gmail.com\",\"dateOfBirth\":531792000.000000000}";


        objectMapper.registerModule(new JavaTimeModule());

        ReservationDTO reservationDTO1 = objectMapper.readValue(message1, ReservationDTO.class);
        ReservationDTO reservationDTO2 = objectMapper.readValue(message2, ReservationDTO.class);
        ReservationDTO reservationDTO3 = objectMapper.readValue(message3, ReservationDTO.class);


        reservationService.makeReservation(reservationDTO1);
        reservationService.makeReservation(reservationDTO2);
        reservationService.makeReservation(reservationDTO3);
    }

}
