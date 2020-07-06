package com.jose.bookingConsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookingConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingConsumerApplication.class, args);
	}

}
