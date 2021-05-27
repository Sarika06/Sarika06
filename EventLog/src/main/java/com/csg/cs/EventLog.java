package com.csg.cs;

import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.csg.cs.service.EventService;

@ComponentScan(basePackages = { "com.csg.cs.service", "com.csg.cs.repository" })
@SpringBootApplication
public class EventLog {
	public static void main(String[] args) throws IOException, ParseException {

		ApplicationContext applicationContext = SpringApplication.run(EventLog.class, args);
		EventService eventService = applicationContext.getBean(EventService.class);
		eventService.logEventData();
	}

}
