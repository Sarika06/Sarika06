package com.csg.cs.test;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.csg.cs.service.EventService;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
	
	EventService eventService = Mockito.mock(EventService.class);
	
	@Test
	public void testLogEventData() throws Exception{
		eventService.logEventData();
		}

}
