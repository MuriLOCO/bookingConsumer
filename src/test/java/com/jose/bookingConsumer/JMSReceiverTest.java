package com.jose.bookingConsumer;

import com.jose.bookingConsumer.service.JMSReceiverService;
import com.jose.bookingConsumer.service.MailSenderService;
import com.jose.bookingConsumer.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JMSReceiverTest {


	@InjectMocks
	private JMSReceiverService jmsReceiverService;

	@Mock
	private ReservationService reservationService;

	@Mock
	private MailSenderService mailSenderService;

	@Test
	public void concurrentCases_success() {
    String message1 = "{\"reservationId\":\"b70c12fb-8c0a-4b06-832c-626beac35366\",\"dayOfReservation\":1594212466.336039100,\"dayOfArrival\":1595940466.336039100,\"dayOfLeaving\":1596199666.336039100,\"cancelled\":false,\"firstMame\":\"Jose\",\"lastName\":\"Mendes\",\"phoneNumber\":\"+15147999483\",\"emailAddress\":\"jose.murilo.mendes@gmail.com\",\"dateOfBirth\":531792000.000000000}";
	String message2 = "{\"reservationId\":\"b70c12fb-8c0a-4b06-832c-626beac35366\",\"dayOfReservation\":1594212466.336039100,\"dayOfArrival\":1595940466.336039100,\"dayOfLeaving\":1596199666.336039100,\"cancelled\":false,\"firstMame\":\"Jose\",\"lastName\":\"Mendes\",\"phoneNumber\":\"+15147999483\",\"emailAddress\":\"jose.murilo.mendes@gmail.com\",\"dateOfBirth\":531792000.000000000}";
	String message3 = "{\"reservationId\":\"b70c12fb-8c0a-4b06-832c-626beac35366\",\"dayOfReservation\":1594212466.336039100,\"dayOfArrival\":1595940466.336039100,\"dayOfLeaving\":1596199666.336039100,\"cancelled\":false,\"firstMame\":\"Jose\",\"lastName\":\"Mendes\",\"phoneNumber\":\"+15147999483\",\"emailAddress\":\"jose.murilo.mendes@gmail.com\",\"dateOfBirth\":531792000.000000000}";
	jmsReceiverService.receiveReservation(message1);
	jmsReceiverService.receiveReservation(message2);
	jmsReceiverService.receiveReservation(message3);


	}

}
